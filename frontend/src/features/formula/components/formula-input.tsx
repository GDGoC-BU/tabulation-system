import { useController, useWatch } from 'react-hook-form'
import useFormulaCriterionLookup from '../hooks/use-formula-criterion-lookup'
import deleteLastFormulaChunk from '../lib/delete-last-formula-chunk'
import FormulaButton from './formula-button'
import FormulaRenderer from './formula-renderer'
import type { Control } from 'react-hook-form'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import FormulaBadgeRenderer from '@/features/formula/components/formula-label-renderer'

type ButtonItem = {
  className: string | null
  label: string
  value: string | null
}

const mainButtonItems: Array<ButtonItem> = [
  { className: 'col-span-2', label: '(', value: ' ( ' },
  { className: 'col-span-2', label: ')', value: ' ) ' },
  { className: '', label: '7', value: '7' },
  { className: '', label: '8', value: '8' },
  { className: '', label: '9', value: '9' },
  { className: '', label: '/', value: ' / ' },
  { className: '', label: '4', value: '4' },
  { className: '', label: '5', value: '5' },
  { className: '', label: '6', value: '6' },
  { className: '', label: '*', value: ' * ' },
  { className: '', label: '1', value: '1' },
  { className: '', label: '2', value: '2' },
  { className: '', label: '3', value: '3' },
  { className: '', label: '-', value: ' - ' },
  { className: '', label: '0', value: '0' },
  { className: '', label: '.', value: '.' },
  { className: 'display-hidden', label: '', value: null },
  { className: '', label: '+', value: ' + ' },
]

export default function FormulaInput({
  name,
  control,
}: {
  name: string
  control: Control<any>
}) {
  const { field } = useController({ name, control })
  const formula = useWatch({ name, control }) || ''

  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  const criterionMap = new Map<string, string>()
  pageantHierarchy?.phases.forEach((phase) => {
    phase.segments.forEach((segment) => {
      segment.criteria.forEach((criterion) => {
        const key = criterion.id
        const value = `<${phase.name} : ${segment.name} : ${criterion.name}>`
        criterionMap.set(key, value)
      })
    })
  })

  const handleButtonInput = (value: string | null) => {
    if (!value) return
    field.onChange(formula + value)
    console.log('Formula MAIN:', '"', formula + value, '"')
    // console.log('------------------------------')
  }

  const handleButtonDelete = () => {
    console.log('Formula A:', '"', formula, '"')
    field.onChange(deleteLastFormulaChunk(formula))
    console.log('Formula B:', '"', field.value, '"')
    console.log('-----------------------------')
  }

  return (
    <div className="border rounded-lg">
      <div className="p-4 min-h-[150px] overflow-x-scroll border-b">
        <FormulaRenderer formula={formula} criterionLookup={criterionLookup} />
      </div>

      <div className="p-4 flex flex-row gap-4">
        <div className="grid grid-cols-4 gap-1">
          <div className="col-span-4 grid grid-cols-3 gap-1">
            <FormulaButton disabled={true}>{'<-'}</FormulaButton>
            <FormulaButton disabled={true}>{'->'}</FormulaButton>
            <FormulaButton onClick={handleButtonDelete}>DEL</FormulaButton>
          </div>

          {mainButtonItems.map((button, index) => {
            return (
              <FormulaButton
                className={button.className ? button.className : ''}
                key={index}
                onClick={() => handleButtonInput(button.value)}
              >
                {button.label}
              </FormulaButton>
            )
          })}
        </div>
        <div className="overflow-y-scroll gap-1 flex flex-col h-[235px]">
          {Object.values(criterionLookup).map((criterionRelationship) => {
            return (
              <FormulaButton
                key={criterionRelationship.criterion.id}
                onClick={() =>
                  handleButtonInput(` ${criterionRelationship.criterion.id}`)
                }
              >
                <FormulaBadgeRenderer
                  simplified={false}
                  criterionRelationship={criterionRelationship}
                />
              </FormulaButton>
            )
          })}
        </div>
      </div>
    </div>
  )
}
