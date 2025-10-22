import { createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import { useQualifiedCandidates } from '@/features/segments/hooks/use-qualified-candidates-query'
import { TextBody, TextHeading } from '@/components/text'
import splitCandidates from '@/features/candidates/lib/split-candidates'

export const Route = createFileRoute(
  '/admin/console/segments/$segmentId/qualified',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { segmentId } = Route.useParams()
  const { data: segment } = useQualifiedCandidates(segmentId)

  const { groupA: candidateGroupA, groupB: candidateGroupB } = useMemo(() => {
    if (!segment) {
      return {
        groupA: [],
        groupB: [],
      }
    }

    return splitCandidates(segment.qualifiedCandidates)
  }, [segment])

  return (
    <div className="m-4 border rounded-lg p-4">
      <div className="mb-18">
        <TextHeading>{segment?.name} Qualified Candidates</TextHeading>
      </div>
      <div className="grid grid-cols-2 gap4">
        <div className="flex flex-col gap-4">
          <div className="text center">
            <TextBody className="font-bold">Female</TextBody>
          </div>
          <div className="flex flex-col gap-2">
            {candidateGroupA.map((candidate) => {
              return (
                <div className="flex flex-row gap-4">
                  <div>
                    <TextBody>Candidate {candidate.number}</TextBody>
                  </div>
                  <div>
                    <TextBody>
                      {candidate.firstName} {candidate.lastName}
                    </TextBody>
                  </div>
                </div>
              )
            })}
          </div>
        </div>
        <div className="flex flex-col gap-4">
          <div className="text center">
            <TextBody className="font-bold">Male</TextBody>
          </div>
          <div className="flex flex-col gap-2">
            {candidateGroupB.map((candidate) => {
              return (
                <div className="flex flex-row gap-2">
                  <div>
                    <TextBody>Candidate {candidate.number}</TextBody>
                  </div>
                  <div>
                    <TextBody>
                      {candidate.firstName} {candidate.lastName}
                    </TextBody>
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      </div>
    </div>
  )
}
