import { useController, useWatch } from 'react-hook-form'
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
  const formula = useWatch({ name, control }) || ''

  return (
    <div className="grow">
      <Workspace />
    </div>
  )
}
