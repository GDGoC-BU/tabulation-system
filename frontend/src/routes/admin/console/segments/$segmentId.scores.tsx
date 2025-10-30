import { createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import { useSegmentQuery } from '@/features/segments/hooks/use-segment-query'
import { useJudgesQuery } from '@/features/judges/hooks/use-judges-query'
import { useCandidatesQuery } from '@/features/candidates/hooks/use-candidates-query'
import { useScoresQuery } from '@/features/scores/hooks/use-scores-query'
import Console from '@/components/console'
import { TextBody, TextHeading } from '@/components/text'
import splitCandidates from '@/features/candidates/lib/split-candidates'
import capitalizeWords from '@/lib/capitalize-words'

export const Route = createFileRoute(
  '/admin/console/segments/$segmentId/scores',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { segmentId } = Route.useParams()
  const { data: judges } = useJudgesQuery()
  const { data: candidates } = useCandidatesQuery()
  const { data: segment } = useSegmentQuery(segmentId)
  const { data: scores } = useScoresQuery(
    {
      segmentId: segment?.id ?? '',
    },
    !!segment,
  )

  const { femaleCandidates, maleCandidates } = useMemo(() => {
    if (!segment) {
      return {
        femaleCandidates: [],
        maleCandidates: [],
      }
    }

    return splitCandidates(candidates ?? [])
  }, [segment])

  return (
    <Console>
      <Console.Header className="">
        <Console.Header.Title>{segment?.name} Scores</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <div className="grid grid-cols-2 gap-4">
          {judges?.map((judge) => {
            return (
              <div className="border rounded-lg p-4 flex flex-col gap-4">
                <div className="mt-4 mb-4 text-center">
                  <TextHeading>
                    {capitalizeWords(judge.honorific) + '.'}{' '}
                    {capitalizeWords(judge.firstName)}{' '}
                    {capitalizeWords(judge.lastName)}
                  </TextHeading>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="flex flex-col  gap-4">
                    <div className="text-center bg-pink-400 w-fit mx-auto px-4 py-2 rounded-full">
                      <TextBody className="text-background">FEMALE</TextBody>
                    </div>
                    {femaleCandidates.map((candidate) => {
                      const candidateScores = scores?.filter(
                        (score) =>
                          score.candidateId === candidate.id &&
                          score.judgeId === judge.id,
                      )

                      return (
                        <div className="border rounded-lg p-4 flex flex-col gap-4">
                          <div className="">
                            <TextBody className="text-lg font-bold">
                              Candidate {candidate.number} :{' '}
                              {candidate.lastName}
                            </TextBody>
                          </div>
                          <div className="">
                            {candidateScores
                              ?.sort(
                                (a, b) =>
                                  b.criterion.maxScore - a.criterion.maxScore,
                              )
                              .map((score) => {
                                return (
                                  <div className="flex flex-row justify-between items-center gap-4">
                                    <div className="">
                                      <TextBody>
                                        {score.criterion.name}
                                      </TextBody>
                                    </div>
                                    <div className="">
                                      <TextBody className="text-lg font-bold">
                                        {score.value} /{' '}
                                        {score.criterion.maxScore}
                                      </TextBody>
                                    </div>
                                  </div>
                                )
                              })}
                          </div>
                        </div>
                      )
                    })}
                  </div>
                  <div className="flex flex-col  gap-4">
                    <div className="text-center bg-blue-400 w-fit mx-auto px-4 py-2 rounded-full">
                      <TextBody className="text-background">MALE</TextBody>
                    </div>
                    {maleCandidates.map((candidate) => {
                      const candidateScores = scores?.filter(
                        (score) =>
                          score.candidateId === candidate.id &&
                          score.judgeId === judge.id,
                      )

                      return (
                        <div className="border rounded-lg p-4 flex flex-col gap-4">
                          <div className="">
                            <TextBody className="text-lg font-bold">
                              Candidate {candidate.number} :{' '}
                              {candidate.lastName}
                            </TextBody>
                          </div>
                          <div className="">
                            {candidateScores
                              ?.sort(
                                (a, b) =>
                                  b.criterion.maxScore - a.criterion.maxScore,
                              )
                              .map((score) => {
                                return (
                                  <div className="flex flex-row justify-between items-center gap-4">
                                    <div className="">
                                      <TextBody>
                                        {score.criterion.name}
                                      </TextBody>
                                    </div>
                                    <div className="">
                                      <TextBody className="text-lg font-bold">
                                        {score.value} /{' '}
                                        {score.criterion.maxScore}
                                      </TextBody>
                                    </div>
                                  </div>
                                )
                              })}
                          </div>
                        </div>
                      )
                    })}
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </Console.Content>
    </Console>
  )
}
