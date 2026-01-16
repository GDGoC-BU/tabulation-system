import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import type { SegmentEditForm } from '@/features/segments/schemas'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'
import Console from '@/components/console'
import FormulaInput from '@/features/formula/components/formula-input'
import { TextSub } from '@/components/text'
import { segmentEditFormSchema } from '@/features/segments/schemas'
import useEditSegmentMutate from '@/features/segments/hooks/use-edit-segment-mutate'
import Loading from '@/components/loading'
import segmentQueryOptions from '@/features/segments/query-options/segment-query-options'

export const Route = createFileRoute('/admin/console/segments/$segmentId/edit')(
  {
    component: RouteComponent,
  },
)

function RouteComponent() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { mutateAsync: editSegment, error } = useEditSegmentMutate()

  /* Check if pageant is selected */
  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  /* Get segment id */
  const { segmentId } = Route.useParams()
  /* Fetch award details if pageant is selected */
  const { data: segment, isLoading: isSegmentLoading } = useQuery(
    segmentQueryOptions(segmentId, {
      enabled: !!selectedPageant,
    }),
  )

  const form = useForm({
    resolver: zodResolver(segmentEditFormSchema),
    defaultValues: {
      id: '',
      name: '',
      qualificationLeaderboard: {
        formula: {
          text: '',
          workspace: null,
        },
        selectionCount: 0,
      },
    },
  })

  /* When segment data arrives, set tthe form values */
  useEffect(() => {
    if (!segment) return
    form.reset({
      id: segment.id,
      name: segment.name,
      qualificationLeaderboard: {
        selectionCount: segment.qualificationLeaderboard
          ? segment.qualificationLeaderboard.selectionCount
          : 0,
        formula: segment.qualificationLeaderboard?.formula,
      },
    })
  }, [segment])

  if (!selectedPageant) {
    navigate({
      to: '/admin/console/pageants',
    })
  }

  if (isSelectedPageantLoading || isSegmentLoading) {
    return (
      <div className="p-4">
        <Loading />
      </div>
    )
  }

  async function onSubmit(values: SegmentEditForm) {
    /* NOTE: These 2 inpuys should have a value or be null together! Not one of each. Refactor later*/
    if (
      values.qualificationLeaderboard!.selectionCount == 0 ||
      values.qualificationLeaderboard!.formula.text.length == 0
    ) {
      values.qualificationLeaderboard = null
    }
    const isSuccess = await editSegment(values)
    if (isSuccess) {
      form.reset()
      form.clearErrors()
      queryClient.invalidateQueries({ queryKey: ['segments'] })
      navigate({ to: '/admin/console/segments' })
    }
  }

  function onEditCancel() {
    form.reset()
    form.clearErrors()
    navigate({ to: '/admin/console/segments' })
  }

  const NameFormField = (
    <FormField
      control={form.control}
      name="name"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Name</FormLabel>
          <FormControl>
            <Input placeholder="Top 5 finalists" {...field} />
          </FormControl>
          <FormMessage />
        </FormItem>
      )}
    />
  )

  const SelectionCountFormField = (
    <FormField
      control={form.control}
      name="qualificationLeaderboard.selectionCount"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Qualifier Count</FormLabel>
          <FormControl>
            <Input type="number" min={0} {...field} />
          </FormControl>
          <FormMessage />
        </FormItem>
      )}
    />
  )

  return (
    <Console>
      <Console.Header className="">
        <Console.Header.Title>Editing {segment?.name}</Console.Header.Title>
      </Console.Header>
      <Console.Content>
        <div className="h-full">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="gap-4 flex h-full flex-col"
            >
              <div className="grid grid-cols-2 gap-4">
                {NameFormField}
                {SelectionCountFormField}
              </div>
              <div className="grow">
                <FormField
                  control={form.control}
                  /* Error message is determined from formula.text, bind <FormulaMessage/> to it */
                  name="qualificationLeaderboard.formula.text"
                  render={() => (
                    <FormItem className="flex flex-col h-full">
                      <FormLabel>Formula</FormLabel>
                      <FormMessage className="" />
                      {/* Actual formula object is still being targeted here */}
                      <FormulaInput
                        initialFormula={
                          segment?.qualificationLeaderboard?.formula
                        }
                        name="qualificationLeaderboard.formula"
                        control={form.control}
                      />
                    </FormItem>
                  )}
                />
              </div>
              {error && <TextSub className="text-destructive">{error}</TextSub>}
              <div className="flex flex-row gap-4">
                <Button type="button" variant="outline" onClick={onEditCancel}>
                  Cancel
                </Button>
                <Button type="submit" variant="default">
                  Save
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </Console.Content>
    </Console>
  )
}
