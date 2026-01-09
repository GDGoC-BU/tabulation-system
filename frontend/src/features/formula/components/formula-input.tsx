import { useController } from 'react-hook-form'
import type { Control } from 'react-hook-form'
import type { Formula } from '../schemas'
import Workspace from '@/features/blockly/workspace'

export default function FormulaInput({
  name,
  control,
  initialFormula,
}: {
  name: string
  control: Control<any>
  initialFormula?: Formula | null
}) {
  const { field } = useController({ name, control })

  /* Called when workspace changes */
  const onFormulaChange = (formula: Formula) => {
    /* Push changes to RHF */
    if (formula.text.trim().length === 0) {
      return field.onChange(null)
    }
    field.onChange({ text: formula.text, workspace: formula.workspace })
  }

  return (
    <div className="grow">
      <Workspace
        /* Recheck if this key prop is actually necessary to keep the component stable */
        key={initialFormula?.text ?? 'new'}
        initialFormula={initialFormula}
        onFormulaChange={onFormulaChange}
      />
    </div>
  )
}
