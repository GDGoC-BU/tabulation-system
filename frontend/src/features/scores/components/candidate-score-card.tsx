import { useEffect, useState } from 'react'
import ScoreEditForm from './score-edit-form'
import type { ScoreDetailed } from '@/features/scores/schemas'
import type {
  CandidateDetailed,
  CandidateHierarchy,
  CandidateSummary,
} from '@/features/candidates/schemas'
import type { ComponentClassNameProp } from '@/types'
import { cn } from '@/lib/utils'
import { TextBody, TextHeading, TextSub } from '@/components/text'
import capitalizeWords from '@/lib/capitalize-words'

export default function CandidateScoreCard({
  candidate,
  scores = [],
  className,
}: {
  candidate: CandidateHierarchy
  scores: Array<ScoreDetailed>
} & ComponentClassNameProp) {
  const [scoreValues, setScoreValues] = useState(() =>
    Object.fromEntries(scores.map((score) => [score.id, score.value])),
  )
  const [totalMaxScore, setTotalMaxScore] = useState(0)
  const [showSavedScorePopup, setShowSavedScorePopup] = useState(false)

  useEffect(() => {
    if (scores.length === 0) return

    setScoreValues(
      Object.fromEntries(scores.map((score) => [score.id, score.value])),
    )
  }, [scores])

  useEffect(() => {
    if (scores.length === 0) return
    setTotalMaxScore(scores.reduce((a, b) => a + b.criterion.maxScore, 0))
  }, [scores])

  /* Prop drill and handle total score count client side to minimize expensive score fetching and keep it simple. */
  const updateTotalScore = (id: string, value: number) => {
    setScoreValues((prev) => ({
      ...prev,
      [id]: value,
    }))

    if (id.length !== 0) {
      setShowSavedScorePopup(true)

      setTimeout(() => {
        setShowSavedScorePopup(false)
      }, 1000)
    }
  }

  const totalScore = Object.values(scoreValues).reduce(
    (sum, val) => sum + (isNaN(val) ? 0 : val),
    0,
  )

  return (
    <div
      className={cn('bg-background rounded-xl border flex flex-col', className)}
    >
      <div className="px-8 py-8 flex flex-col items-center gap-2 justify-center w-full border-b">
        <div className="[&>*]:text-center flex flex-col items-center">
          <div className="bg-black w-fit px-4 aspect-square rounded-full grid place-items-center">
            <TextHeading className="text-background">
              {candidate.number}
            </TextHeading>
          </div>
          <TextBody className="mt-2">
            {capitalizeWords(candidate.firstName)}{' '}
            {capitalizeWords(candidate.lastName)}
          </TextBody>
          <TextSub className="mt-0">{candidate.college.name}</TextSub>
        </div>
        <div className="size-fit relative">
          <div className="border size-fit px-2 py-1 rounded-full flex flex-row gap-1 items-center">
            <TextSub className="text-foreground">
              {totalScore} / {totalMaxScore}
            </TextSub>
          </div>
          <div
            className={cn(
              'absolute rotate-[0deg] right-[50%] translate-x-[50%] bg-emerald-500 px-[6px] pt-[0px] pb-[2px] rounded-full transition-all',
              showSavedScorePopup
                ? 'opacity-100 scale-100 top-[-40%]'
                : 'opacity-0 scale-0 top-[0%]',
            )}
          >
            <TextSub className="text-white text-[10px]">Saved!</TextSub>
          </div>
        </div>
      </div>
      <div className="px-8 py-8 flex flex-col gap-2 w-fit">
        {scores.map((score) => {
          return (
            <ScoreEditForm
              className="w-max min-w-full"
              key={score.id}
              score={score}
              updateTotalScore={updateTotalScore}
            />
          )
        })}
      </div>
    </div>
  )
}
