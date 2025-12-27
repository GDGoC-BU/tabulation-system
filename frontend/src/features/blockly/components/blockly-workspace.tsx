import * as Blockly from 'blockly'
import { useEffect, useRef } from 'react'

export default function BlocklyWorkspace() {
  const blocklyRef = useRef<HTMLDivElement | null>(null)
  const workspaceRef = useRef<Blockly.WorkspaceSvg | null>(null)

  useEffect(() => {
    if (!blocklyRef.current) return

    workspaceRef.current = Blockly.inject(blocklyRef.current, {
      toolbox: {
        kind: 'flyoutToolbox',
        contents: [],
      },
      trashcan: true,
      scrollbars: true,
    })

    return () => {
      workspaceRef.current?.dispose()
    }
  })

  return <div ref={blocklyRef} style={{ width: '100%', height: '100%' }} />
}
