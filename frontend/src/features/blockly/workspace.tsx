import './blocks'
import './generators'

import { useEffect, useRef } from 'react'
import * as Blockly from 'blockly'
import { useQuery } from '@tanstack/react-query'
import { javascriptGenerator } from 'blockly/javascript'
import pageantHierarchyQueryOptions from '../pageants/query-options/pageant-hierarchy-query-options'
import { useSelectedPageant } from '../pageants/hooks/use-selected-pageant'
import { generateCriterionLookup } from '../criteria/lib/generate-criterion-lookup'
import { toolbox } from './toolbox'
import { useBlocklyStore } from './store/use-blockly-store'
import type { Abstract } from 'node_modules/blockly/core/events/events_abstract'
import { Button } from '@/components/ui/button'

export default function Workspace({
  onFormulaChange,
}: {
  onFormulaChange?: (value: string) => void
}) {
  /* Refs to inject Blockly workspace*/
  const blocklyRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)

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
  }, [pageantHierarchy])

  useEffect(() => {
    /* If workspace div hasn't mounted */
    if (!blocklyRef.current) return
    /* If workspace has already been injected */
    if (workspaceRef.current) return

    /* Inject the workspace */
    workspaceRef.current = Blockly.inject(blocklyRef.current, {
      toolbox,
      trashcan: true,
      scrollbars: true,
    })
    const workspace = workspaceRef.current

    /* Trigger the callback on workspace change
    Note: This listener runs on ALL workspace changes. */
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

      /* Initialize the code-generator and generate the code */
      javascriptGenerator.init(workspace)
      const value = javascriptGenerator.blockToCode(formulaRootBlock, true)
      /* Output could be: 
      1) Values: [String, Order]
      2) Statements: String
      Only extract the final statement */
      const formula = Array.isArray(value) ? 'Statement' : value
      onFormulaChange(formula)
    }
    workspace.addChangeListener(onFormulaChangeListener)

    /* Append the root level formula block */
    Blockly.serialization.blocks.append({ type: 'formula_root' }, workspace)

    return () => {
      workspace.dispose()
      workspace.removeChangeListener(onFormulaChangeListener)
      workspaceRef.current = null
    }
  }, [])

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
      <div ref={blocklyRef} className="h-full z-[998]" />
    </div>
  )
}
