import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import type { AwardEditForm } from '@/features/awards/schemas'
import { useAwardQuery } from '@/features/awards/hooks/use-award-query'
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
import { awardEditFormSchema } from '@/features/awards/schemas'
import Console from '@/components/console'
import FormulaInput from '@/features/formula/deprecated-components/formula-input'
import { TextSub } from '@/components/text'
import useEditAwardMutate from '@/features/awards/hooks/use-edit-award-mutate'
import Loading from '@/components/loading'

export const Route = createFileRoute('/admin/console/awards/$awardId/edit')({
  component: RouteComponent,
})

function RouteComponent() {
  const { awardId } = Route.useParams()
  const { data: award } = useAwardQuery(awardId)
  const { mutateAsync: editAward, error } = useEditAwardMutate()
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { data: selectedPageant, isLoading } = useSelectedPageant()

  const form = useForm({
    resolver: zodResolver(awardEditFormSchema),
    defaultValues: {
      id: '',
      name: '',
      candidateLimit: '',
      formula: '',
    },
  })

  useEffect(() => {
    if (!award) return
    form.reset({
      id: award.id,
      name: award.name,
      candidateLimit: award.candidateLimit,
      formula: award.formula,
    })
  }, [award])

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

  async function onSubmit(values: AwardEditForm) {
    const isSuccess = await editAward(values)
    if (isSuccess) {
      form.reset()
      form.clearErrors()
      queryClient.invalidateQueries({ queryKey: ['awards'] })
      navigate({ to: '/admin/console/awards' })
    }
  }

  function onEditCancel() {
    form.reset()
    form.clearErrors()
    navigate({ to: '/admin/console/awards' })
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
          <FormLabel>Number of Candidates</FormLabel>
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
        <Console.Header.Title>Editing {award?.name}</Console.Header.Title>
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
