import { useController } from 'react-hook-form'
import type { Control } from 'react-hook-form'
import Workspace from '@/features/blockly/workspace'

export default function FormulaInput({
  name,
  control,
}: {
  name: string
  control: Control<any>
}) {
  const { field } = useController({ name, control })

  const onFormulaChange = (f: {
    text: string
    serialized: { [key: string]: any }
  }) => {
    field.onChange({ text: f.text, serialized: f.serialized })
  }

  return (
    <div className="grow">
      <Workspace onFormulaChange={onFormulaChange} />
    </div>
  )
}
