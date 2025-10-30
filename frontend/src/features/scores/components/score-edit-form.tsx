import { useEffect, useState } from 'react'
import type { ScoreDetailed } from '@/features/scores/schemas'
import { TextBody, TextSub } from '@/components/text'
import { Input } from '@/components/ui/input'
import useDebounce from '@/hooks/use-debounce'
import useEditScoreMutate from '@/features/scores/hooks/use-edit-score-mutate'

export default function ScoreEditForm({
  score,
  onChangeScore,
}: {
  score: ScoreDetailed
  onChangeScore: (id: string, value: number) => void
}) {
  const [scoreValue, setScoreValue] = useState(score.value)
  const [isError, setIsError] = useState(false)
  const debouncedScore = useDebounce(scoreValue, 1500)
  const { mutateAsync: editScore } = useEditScoreMutate()

  useEffect(() => {
    if (isNaN(debouncedScore) || debouncedScore > score.criterion.maxScore) {
      setIsError(true)
      return
    }
    setIsError(false)

    const changeScore = async () => {
      const updatedScore = await editScore({
        id: score.id,
        value: debouncedScore,
      })
      onChangeScore(score.id, updatedScore.value)
    }
    changeScore()
  }, [debouncedScore])

  return (
    <div className="flex flex-col items-center ">
      <div className="grid grid-cols-2 gap-4 items-center">
        <div className="justify-self-end">
          <TextBody className="">{score.criterion.name}</TextBody>
          {isError && (
            <TextSub className="text-destructive text-end">
              Invalid Score
            </TextSub>
          )}
        </div>
        <div className="justify-self-start flex flex-row gap-4 items-center">
          <div className="w-fit">
            <Input
              defaultValue={score.value}
              type="number"
              min={0}
              max={score.criterion.maxScore}
              onChange={(e) => setScoreValue(parseInt(e.target.value))}
            />
          </div>
          <TextSub>Max: {score.criterion.maxScore}</TextSub>
        </div>
      </div>
    </div>
  )
}
