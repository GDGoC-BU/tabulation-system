import './blocks'
import './generators'

import { useEffect, useRef } from 'react'
import * as Blockly from 'blockly'
import { javascriptGenerator } from 'blockly/javascript'
import { useQuery } from '@tanstack/react-query'
import pageantHierarchyQueryOptions from '../pageants/query-options/pageant-hierarchy-query-options'
import { useSelectedPageant } from '../pageants/hooks/use-selected-pageant'
import { generateCriterionLookup } from '../criteria/lib/generate-criterion-lookup'
import { toolbox } from './toolbox'
import { useBlocklyStore } from './store/use-blockly-store'
import { Button } from '@/components/ui/button'

export default function Workspace() {
  const blocklyRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)

  const { setCriterionLookup } = useBlocklyStore((state) => state)

  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant?.id, {
        enabled: !!selectedPageant,
        staleTime: 0,
      }),
    )

  useEffect(() => {
    if (!pageantHierarchy) return
    setCriterionLookup(generateCriterionLookup(pageantHierarchy.phases))
  }, [pageantHierarchy])

  useEffect(() => {
    if (!blocklyRef.current) return

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
    }
  })

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
        <Button onClick={generateCode}>Generate Code</Button>
        <Button onClick={getTopBlocks}>Get top blocks</Button>
        <Button onClick={getJSON}>Get JSON</Button>
      </div>
      <div ref={blocklyRef} className="h-full z-[998]" />
    </div>
  )
}

/*

export default function Workspace() {
  const blocklyRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)

  const { setCriterionLookup } = useCriterionDropdownStore((state) => state)

  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    useQuery(
      pageantHierarchyQueryOptions(selectedPageant?.id, {
        enabled: !!isSelectedPageantLoading,
        staleTime: 0,
      }),
    )

  useEffect(() => {
    if (!pageantHierarchy) return

    setCriterionLookup(generateCriterionLookup(pageantHierarchy.phases))
  }, [pageantHierarchy])

  useEffect(() => {
    if (!blocklyRef.current) return

    workspaceRef.current = Blockly.inject(blocklyRef.current, {
      toolbox,
      trashcan: true,
      scrollbars: true,
    })

    return () => {
      workspaceRef.current?.dispose()
    }
  })

  if (isSelectedPageantLoading || isPageantHierarchyLoading) {
    return (
      <div className="">
        <h1 className="">Workspace loading...</h1>
      </div>
    )
  }

  return (
    <div className="h-full relative">
      <div ref={blocklyRef} className="h-full z-[998]" />
    </div>
  )
}

*/
