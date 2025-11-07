import { createFileRoute } from '@tanstack/react-router'
import { useSegmentCalculateQualifiedCandidates } from '@/features/segments/hooks/use-qualified-candidates-query'
import { TextBody, TextHeading } from '@/components/text'
import { groupCandidateQualificationsByGender } from '@/lib/group-candidate-qualifications-by-gender'
import FormulaRenderer from '@/features/formula/components/formula-renderer'
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import Table from '@/components/table'
import { segmentCandidateQualifications } from '@/features/segments/components/segment-candidate-qualifications-table-columns'
import Loading from '@/components/loading'

export const Route = createFileRoute(
  '/admin/console/segments/$segmentId/qualified',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { segmentId } = Route.useParams()
  const {
    data: segment,
    isLoading: isSegmentLoading,
    isError: isSegmentError,
    error: segmentError,
  } = useSegmentCalculateQualifiedCandidates(segmentId)

  const { data: selectedPageant, isLoading: isPageantStateLoading } =
    useSelectedPageantQuery()
  const { data: pageantHierarchy, isLoading: isPageantHierarchyLoading } =
    usePageantHierarchyQuery(selectedPageant?.id)
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  if (isSegmentError) {
    return (
      <div className="border rounded-lg p-4 w-full">
        <TextBody>{segmentError}</TextBody>
      </div>
    )
  }

  if (isPageantStateLoading || isPageantHierarchyLoading) {
    return (
      <div className="border rounded-lg p-4 w-full">
        <Loading />
      </div>
    )
  }

  if (isSegmentLoading) {
    return (
      <div className="border rounded-lg p-4 w-full">
        <TextBody>Calculating qualified candidates...</TextBody>
      </div>
    )
  }

  const { groupA, groupB } = groupCandidateQualificationsByGender(
    segment?.candidateQualifications,
  )

  return (
    <div className="p-4 w-full">
      <div className="w-full flex flex-col gap-24">
        <div className="border rounded-lg p-4">
          <div className="mb-2">
            <TextHeading>Qualified Candidates for {segment?.name}</TextHeading>
          </div>
          <div className="flex flex-col gap-2 border-b pb-8">
            <div className="">
              <TextBody>
                Candidate Limit: {segment?.candidateLimit ?? 'None'}
              </TextBody>
            </div>
            <div className="">
              {segment?.formula === null ? (
                <div>
                  <TextBody>Formula: None</TextBody>
                </div>
              ) : (
                <div className="flex flex-row gap-2">
                  <TextBody>Formula: </TextBody>
                  <FormulaRenderer
                    formula={segment?.formula ?? ''}
                    criterionLookup={criterionLookup}
                  />
                </div>
              )}
            </div>
          </div>
          <div className="w-full flex flex-col">
            <div className="flex flex-col gap-2 mt-8">
              <div className="w-full text-center mb-8">
                <TextHeading className="font-bold">Female</TextHeading>
              </div>
              <Table.CandidateQualifications
                columns={segmentCandidateQualifications}
                data={groupA}
                limit={segment?.candidateLimit ?? 0}
                formula={segment?.formula ?? ''}
              />
            </div>
            <div className="flex flex-col gap-2 mt-8">
              <div className="w-full text-center mb-8">
                <TextHeading className="font-bold">Male</TextHeading>
              </div>
              <Table.CandidateQualifications
                columns={segmentCandidateQualifications}
                data={groupB}
                limit={segment?.candidateLimit ?? null}
                formula={segment?.formula ?? null}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
