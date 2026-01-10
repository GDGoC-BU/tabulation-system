import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useEffect } from 'react'
import type { AwardEditForm } from '@/features/awards/schemas'
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
import FormulaInput from '@/features/formula/components/formula-input'
import { TextSub } from '@/components/text'
import useEditAwardMutate from '@/features/awards/hooks/use-edit-award-mutate'
import Loading from '@/components/loading'
import awardQueryOptions from '@/features/awards/query-options/award-query-options'

export const Route = createFileRoute('/admin/console/awards/$awardId/edit')({
  component: RouteComponent,
})

function RouteComponent() {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { mutateAsync: editAward, error } = useEditAwardMutate()

  /* Check if a pageant is selecteed */
  const { data: selectedPageant, isLoading: isSelectedPageantLoading } =
    useSelectedPageant()
  /* Get award id */
  const { awardId } = Route.useParams()
  /* Fetch award details if pageant is selected */
  const { data: award, isLoading: isAwardLoading } = useQuery(
    awardQueryOptions(awardId, {
      enabled: !!selectedPageant,
    }),
  )

  const form = useForm({
    resolver: zodResolver(awardEditFormSchema),
    defaultValues: {
      id: '',
      name: '',
      candidateLimit: '',
      formula: {
        text: '',
        workspace: {},
      },
    },
  })

  /* When the Award data arrives, set the form values */
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

  if (isSelectedPageantLoading || isAwardLoading) {
    return (
      <div className="p-4">
        <Loading />
      </div>
    )
  }

  async function onSubmit(values: AwardEditForm) {
    if (values.candidateLimit === 0) {
      values.candidateLimit = null
    }
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
        <div className="h-full">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="gap-4 flex h-full flex-col"
            >
              <div className="grid grid-cols-2 gap-4">
                {NameFormField}
                {CandidateLimitFormField}
              </div>
              <div className="grow">
                <FormField
                  control={form.control}
                  /* Error message is determined from formula.text, bind <FormulaMessage/> to it */
                  name="formula.text"
                  render={() => (
                    <FormItem className="flex flex-col h-full">
                      <FormLabel>Formula</FormLabel>
                      <FormMessage className="" />
                      {/* Actual formula object is still being targeted here */}
                      <FormulaInput
                        initialFormula={award?.formula}
                        name="formula"
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
