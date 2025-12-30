import './blocks'
import './generators'

import { useEffect, useRef } from 'react'
import * as Blockly from 'blockly'
import { javascriptGenerator } from 'blockly/javascript'
import { toolbox } from './toolbox'
import { Button } from '@/components/ui/button'

export default function BlocklyWorkspace() {
  const blocklyRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)

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
