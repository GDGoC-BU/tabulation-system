import { useEffect, useRef, useState } from 'react'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type { ComponentClassNameProp } from '@/types'
import { TextBody, TextSub } from '@/components/text'
import { Input } from '@/components/ui/input'
import useDebounce from '@/hooks/use-debounce'
import useEditScoreMutate from '@/features/scores/hooks/use-edit-score-mutate'
import { cn } from '@/lib/utils'

export default function ScoreEditForm({
  score,
  updateTotalScore,
  className,
}: {
  score: ScoreDetailed
  updateTotalScore: (id: string, value: number) => void
} & ComponentClassNameProp) {
  const [scoreValue, setScoreValue] = useState<number | null>(null)
  const [isInputInvalid, setIsInputInvalid] = useState(false)
  const debouncedScore = useDebounce(scoreValue, 1500)
  const {
    mutateAsync: editScore,
    isError: isScoreEditError,
    error: scoreEditError,
  } = useEditScoreMutate()

  /* Track the last value we consider authoritative, either from parent or last save */
  const lastSavedRef = useRef<number | null>(score.value)

  /* Whenever parent sends a new score.value, sync the input and lastSavedRef */
  useEffect(() => {
    setScoreValue(score.value)
    lastSavedRef.current = score.value
  }, [score.value])

  useEffect(() => {
    /* Extra hooks above ensure that on the initial mount, the inputs dont send an 
       edit request! Saving on initial mount resaves all the scores leading to:
       score overwrites and unnecessary requests (again, handling scores are expensinve).  */

    if (debouncedScore === null) return

    /* Validate input */
    if (isNaN(debouncedScore) || debouncedScore > score.criterion.maxScore) {
      setIsInputInvalid(true)
      return
    }

    /* If the debounced value is the same from the last saved/authoritative value, dont save */
    if (debouncedScore === lastSavedRef.current) return

    setIsInputInvalid(false)

    const changeScore = async () => {
      try {
        const updatedScore = await editScore({
          id: score.id,
          value: debouncedScore,
        })

        /* Update the last saved/authoritative value after successful save */
        lastSavedRef.current = updatedScore.value
        updateTotalScore(score.id, updatedScore.value)
      } catch (error) {
        console.error('Failed to save score: ', error)
      }
    }

    changeScore()
  }, [debouncedScore])

  return (
    <div className={cn('grid grid-cols-2 gap-4 items-center', className)}>
      <div className="justify-self-end">
        <TextBody className="text-end whitespace-nowrap">
          {score.criterion.name}
        </TextBody>
        {isInputInvalid && (
          <TextSub className="text-destructive text-end">Invalid Score</TextSub>
        )}
        {isScoreEditError && (
          <TextSub className="text-destructive text-end">
            {scoreEditError}
          </TextSub>
        )}
      </div>
      <div className="justify-self-start flex flex-row gap-4 items-center">
        <div className="w-fit">
          <Input
            defaultValue={
              scoreValue === null || isNaN(scoreValue) ? '' : scoreValue
            }
            type="number"
            className="!text-[16px]"
            min={0}
            max={score.criterion.maxScore}
            onChange={(e) => {
              const raw = e.target.value
              setScoreValue(parseInt(raw))
            }}
          />
        </div>
        <TextSub>Max: {score.criterion.maxScore}</TextSub>
      </div>
    </div>
  )
}
