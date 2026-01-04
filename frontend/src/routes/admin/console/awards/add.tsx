import { Link, createFileRoute, useNavigate } from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useQueryClient } from '@tanstack/react-query'
import type { AwardAddForm } from '@/features/awards/schemas'
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
import { awardAddFormSchema } from '@/features/awards/schemas'
import Console from '@/components/console'
import useAddAwardMutate from '@/features/awards/hooks/use-add-award-mutate'
import FormulaInput from '@/features/formula/components/formula-input'

export const Route = createFileRoute('/admin/console/awards/add')({
  component: AdminConsoleAwardsAdd,
})

function AdminConsoleAwardsAdd() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { mutateAsync: addAward } = useAddAwardMutate()
  const { data: selectedPageant } = useSelectedPageant()

  const form = useForm({
    resolver: zodResolver(awardAddFormSchema),
    defaultValues: {
      name: '',
      candidateLimit: '',
      formula: '',
    },
  })

  if (!selectedPageant) {
    navigate({
      to: '/admin/console/pageants',
    })
  }

  async function onSubmit(values: AwardAddForm) {
    const isSuccess = await addAward(values)
    if (isSuccess) {
      form.reset()
      form.clearErrors()
      queryClient.invalidateQueries({ queryKey: ['awards'] })
      navigate({ to: '/admin/console/awards' })
    }
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
        <Console.Header.Title>
          Add Award for {selectedPageant?.title}
        </Console.Header.Title>
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
                  name="formula"
                  render={() => (
                    <FormItem className="flex flex-col h-full">
                      <FormLabel>Formula</FormLabel>
                      <FormMessage className="" />
                      <FormulaInput name="formula" control={form.control} />
                    </FormItem>
                  )}
                />
              </div>
              {/* {isError && <TextSub className="text-destructive">{error}</TextSub>} */}
              <div className="flex flex-row gap-4">
                <Button type="button" variant="outline">
                  <Link to="/admin/console/awards">Cancel</Link>
                </Button>
                <Button type="submit" variant="default">
                  Create
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </Console.Content>
    </Console>
  )
}
