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
  const { segmentId } = Route.useParams()
  const { mutateAsync: editSegment, error } = useEditSegmentMutate()
  const { data: segment } = useQuery(segmentQueryOptions(segmentId))

  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { data: selectedPageant, isLoading } = useSelectedPageant()

  const form = useForm({
    resolver: zodResolver(segmentEditFormSchema),
    defaultValues: {
      id: '',
      name: '',
      candidateLimit: null,
      formula: null,
    },
  })

  useEffect(() => {
    if (!segment) return
    form.reset({
      id: segment.id,
      name: segment.name,
      candidateLimit: segment.candidateLimit,
      formula: segment.formula,
    })
  }, [segment])

  if (!selectedPageant) {
    navigate({
      to: '/admin/console/pageants',
    })
  }

  if (isLoading) {
    return (
      <div className="p-4">
        <Loading />
      </div>
    )
  }

  async function onSubmit(values: SegmentEditForm) {
    if (values.candidateLimit === 0) {
      values.candidateLimit = null
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

  const CandidateLimitFormField = (
    <FormField
      control={form.control}
      name="candidateLimit"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Candidate Limit</FormLabel>
          <FormControl>
            <Input type="number" min={1} {...field} />
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
        <div className="">
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                {NameFormField}
                {CandidateLimitFormField}
              </div>
              <div className="">
                <FormField
                  control={form.control}
                  name="formula"
                  render={() => (
                    <FormItem>
                      <FormLabel>Formula</FormLabel>
                      <FormMessage className="" />
                      <FormulaInput name="formula" control={form.control} />
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
