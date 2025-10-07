import type { FormulaCriterion } from '../hooks/use-formula-criterion-lookup'
import { TextBody } from '@/components/text'

export default function FormulaBadgeRenderer({
  criterionRelationship,
  simplified = false,
}: {
  criterionRelationship: FormulaCriterion
  simplified?: boolean
}) {
  if (simplified) {
    return (
      <TextBody className="flex flex-row gap-2">
        <span className="">P{criterionRelationship.phase.sequence}</span>
        <span className="">/</span>
        <span className="">{criterionRelationship.segment.name}</span>
        <span className="">/</span>
        <span className="">{criterionRelationship.criterion.name}</span>
      </TextBody>
    )
  }

  return (
    <TextBody className="flex flex-row gap-2">
      <span className="">
        ({' P'}
        {criterionRelationship.phase.sequence} ){' '}
        {criterionRelationship.phase.name}
      </span>
      <span className="">/</span>
      <span className="">{criterionRelationship.segment.name}</span>
      <span className="">/</span>
      <span className="">{criterionRelationship.criterion.name}</span>
    </TextBody>
  )
}
