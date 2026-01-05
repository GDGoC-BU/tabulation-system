import { useController } from 'react-hook-form'
import type { Control } from 'react-hook-form'
import type { Formula } from '../schemas'
import Workspace from '@/features/blockly/workspace'

export default function FormulaInput({
  name,
  control,
}: {
  name: string
  control: Control<any>
}) {
  const { field } = useController({ name, control })

  const onFormulaChange = (formula: Formula) => {
    field.onChange({ text: formula.text, workspace: formula.workspace })
  }

  return (
    <div className="grow">
      <Workspace onFormulaChange={onFormulaChange} />
    </div>
  )
}
