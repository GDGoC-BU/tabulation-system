import './blocks'
import './generators'

import { useEffect, useRef, useState } from 'react'
import * as Blockly from 'blockly'
import { useQuery } from '@tanstack/react-query'
import { javascriptGenerator } from 'blockly/javascript'
import pageantHierarchyQueryOptions from '../pageants/query-options/pageant-hierarchy-query-options'
import { useSelectedPageant } from '../pageants/hooks/use-selected-pageant'
import { generateCriterionLookup } from '../criteria/lib/generate-criterion-lookup'
import { toolbox } from './toolbox'
import { useBlocklyStore } from './store/use-blockly-store'
import type { Abstract } from 'node_modules/blockly/core/events/events_abstract'
import type { Formula } from '../formula/schemas'
import { Button } from '@/components/ui/button'

export default function Workspace({
  initialFormula,
  onFormulaChange,
}: {
  initialFormula?: Formula | undefined | null
  onFormulaChange?: (formula: Formula) => void
}) {
  /* Refs to inject Blockly workspace */
  const workspaceContainerRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)
  /* Track readiness */
  const [isWorkspaceInjected, setIsWorkspaceInjected] = useState(false)
  const [isCriterionDropdownSet, setIsCriterionDropdownSet] = useState(false)
  const [
    isOnFormulaChangeListenerAttached,
    setIsOnFormulaChangeListenerAttached,
  ] = useState(false)

  /* Fetch Pageant criterions */
  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant!.id, {
        enabled: !isSelectedPageantLoading,
        staleTime: 0,
      }),
    )

  /* If there is an initialFormula passed, load that when everything is ready */
  useEffect(() => {
    if (!initialFormula) return
    if (!workspaceRef.current) return
    if (!isWorkspaceInjected) return
    if (!isCriterionDropdownSet) return
    if (!isOnFormulaChangeListenerAttached) return

    /* NOTE: Case where a serialized workspace contains criterions that no longer 
    exist in the criterion dropdown is not yet handled! Say the pageant has 
    criterions: [a,b,c] and a formula has: "a + c", then the admin deletes 
    criterion "c", so the dropdown no longer contains option "c". As per my research,
    blockly still attaches the value, but is no longer displayed as an option when the
    dropdown is opened. */
    Blockly.serialization.workspaces.load(
      initialFormula.workspace as { [key: string]: any },
      workspaceRef.current,
    )
  }, [
    initialFormula,
    isWorkspaceInjected,
    isCriterionDropdownSet,
    isOnFormulaChangeListenerAttached,
  ])

  /* Intialize and inject workspace */
  useEffect(() => {
    /* If workspace div hasn't mounted */
    if (!workspaceContainerRef.current) return

    /* Inject the workspace */
    workspaceRef.current = Blockly.inject(workspaceContainerRef.current, {
      toolbox,
      trashcan: true,
      scrollbars: true,
    })
    const workspace = workspaceRef.current

    /* Append the root level formula block */
    Blockly.serialization.blocks.append({ type: 'formula_root' }, workspace)

    setIsWorkspaceInjected(true)
    return () => {
      workspace.dispose()
      workspaceRef.current = null
    }
  }, [])

  /* Attach onFormulaChangeListener */
  useEffect(() => {
    if (!workspaceRef.current) return

    const workspace = workspaceRef.current
    /* Trigger the callback on workspace change
    Note: This listener runs on ALL workspace changes.
    Find a way to debounce this later or take a different approach */
    const onFormulaChangeListener = (event: Abstract) => {
      if (!onFormulaChange) return
      if (event.isUiEvent) return
      if (
        event.type !== Blockly.Events.BLOCK_CHANGE &&
        event.type !== Blockly.Events.BLOCK_MOVE &&
        event.type !== Blockly.Events.BLOCK_CREATE &&
        event.type !== Blockly.Events.BLOCK_DELETE
      ) {
        return
      }

      /* Get the formula_root */
      const formulaRootBlock = workspace
        .getTopBlocks(false)
        .find((b) => b.type === 'formula_root')
      if (!formulaRootBlock) return

      javascriptGenerator.init(workspace)

      /* Generate the code for formula_root */
      const value = javascriptGenerator.blockToCode(formulaRootBlock, true)
      /* Output could be: 
      1) Values: [String, Order]
      2) Statements: String
      Only extract the final statement */
      const formulaText = Array.isArray(value) ? 'Statement' : value

      /* Serialize the whole workspace.
      NOTE: This saves the whole state of the workspace,
      even blocks not attached to formula_root. This feature can be
      kept, or remove the other top blocks and just keep formula_root.
      This can also be considered as a feature, where the admin can draft
      blocks but not attach it to formula_root */
      const json = Blockly.serialization.workspaces.save(workspace)

      onFormulaChange({ text: formulaText, workspace: json })
    }

    workspace.addChangeListener(onFormulaChangeListener)

    setIsOnFormulaChangeListenerAttached(true)
    return () => {
      workspace.removeChangeListener(onFormulaChangeListener)
    }
  }, [])

  /* Set dynamic criterion dropdown options */
  const { setCriterionDropdownOptions } = useBlocklyStore((state) => state)
  useEffect(() => {
    if (!pageantHierarchy) return

    /* Covert the Record to an array and sort by phase-segment sequence */
    const sortedCriterions = Object.values(
      generateCriterionLookup(pageantHierarchy.phases),
    ).sort((a, b) => {
      if (a.phase.sequence !== b.phase.sequence) {
        return a.phase.sequence - b.phase.sequence
      }
      return a.segment.sequence - b.segment.sequence
    })

    let currentSequence: number | null = null
    const criterionDropdownOptions: Array<[string, string] | 'separator'> = []

    /* Create the criterion dropdown options */
    sortedCriterions.forEach((criterion) => {
      /* Add separator after each segment */
      if (currentSequence === null) {
        currentSequence = criterion.segment.sequence
      }
      if (currentSequence != criterion.segment.sequence) {
        criterionDropdownOptions.push('separator')
        currentSequence = criterion.segment.sequence
      }

      criterionDropdownOptions.push([
        `${criterion.phase.name} / ${criterion.segment.name} / ${criterion.criterion.name}`,
        `${criterion.criterion.id}`,
      ])
    })

    setCriterionDropdownOptions(criterionDropdownOptions)
    setIsCriterionDropdownSet(true)
  }, [pageantHierarchy])

  /* Debugging buttons */
  const generateCode = () => {
    if (!workspaceRef.current) return ''
    const code = javascriptGenerator.workspaceToCode(workspaceRef.current)
    console.log(code)
    return code
  }
  const getTopBlocks = () => {
    if (!workspaceRef.current) return
    const root = workspaceRef.current.getTopBlocks(true)
    console.log(root)
  }
  const getJSON = () => {
    if (!workspaceRef.current) return
    const json = Blockly.serialization.workspaces.save(workspaceRef.current)
    console.log(json)
  }

  if (isSelectedPageantLoading || isPageantHierarchyLoading) {
    return (
      <div className="">
        <h1 className="">Workspace loading...</h1>
      </div>
    )
  }

  return (
    <div className="h-full relative">
      <div className="absolute top-4 right-4 z-[999] flex flex-col gap-4">
        <Button type="button" onClick={generateCode}>
          Generate Code
        </Button>
        <Button type="button" onClick={getTopBlocks}>
          Get top blocks
        </Button>
        <Button type="button" onClick={getJSON}>
          Get JSON
        </Button>
      </div>
      <div ref={workspaceContainerRef} className="h-full z-[998]" />
    </div>
  )
}
