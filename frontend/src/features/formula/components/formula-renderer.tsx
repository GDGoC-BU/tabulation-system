import tokenizeFormula from '../lib/tokenize-formula'
import FormulaBadgeRenderer from './formula-label-renderer'
import type { FormulaCriterionLookup } from '../hooks/use-formula-criterion-lookup'
import { TextBody } from '@/components/text'
import { Badge } from '@/components/ui/badge'

export default function FormulaRenderer({
  formula,
  criterionLookup,
}: {
  formula: string
  criterionLookup: FormulaCriterionLookup
}) {
  if (Object.keys(criterionLookup).length === 0) {
    return
  }

  const tokens = tokenizeFormula(formula)

  return (
    <div className="flex flex-row gap-2 items-center flex-wrap">
      {tokens.map((token, index) => {
        if (token.type === 'uuid') {
          return (
            <Badge key={index} variant="outline" className="bg-muted">
              <FormulaBadgeRenderer
                simplified={false}
                criterionRelationship={criterionLookup[token.value]}
              />
            </Badge>
          )
        }
        return <TextBody>{token.value}</TextBody>
      })}
    </div>
  )
}
