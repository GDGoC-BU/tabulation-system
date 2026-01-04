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
import { Button } from '@/components/ui/button'

export default function Workspace() {
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
    /* If workspace div hasnt mounted */
    if (!blocklyRef.current) return
    /* If workspace has already been injected */
    if (workspaceRef.current) return

    workspaceRef.current = Blockly.inject(blocklyRef.current, {
      toolbox,
      trashcan: true,
      scrollbars: true,
    })
    Blockly.serialization.blocks.append(
      { type: 'formula_root' },
      workspaceRef.current,
    )

    return () => {
      workspaceRef.current?.dispose()
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
