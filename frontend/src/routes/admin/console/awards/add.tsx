import { Link, createFileRoute, useNavigate } from '@tanstack/react-router'
import { useState } from 'react'
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
import { useSelectedPageantQuery } from '@/features/pageants/hooks/use-selected-pageant-query'
import { awardAddFormSchema } from '@/features/awards/schemas'
import Console from '@/components/console'
import FormulaButton from '@/features/formula/components'
import { usePhasesQuery } from '@/features/phases/hooks/use-phases-query'
import { useSegmentsQuery } from '@/features/segments/hooks/use-segments-query'

export const Route = createFileRoute('/admin/console/awards/add')({
  component: AdminConsoleAwardsAdd,
})

function AdminConsoleAwardsAdd() {
  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: phases } = usePhasesQuery()
  const { data: segments } = useSegmentsQuery()

  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const form = useForm<AwardAddForm>({
    resolver: zodResolver(awardAddFormSchema),
    defaultValues: {
      name: '',
      candidateLimit: 0,
      formula: '',
    },
  })

  async function onSubmit(values: AwardAddForm) {
    console.log('Award Values: ', values)
  }

  const onOpenChange = (open: boolean) => {
    setIsDialogOpen(open)
  }

  if (!selectedPageant) {
    navigate({
      to: '/admin/console/pageants',
    })
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
        <div className="max-w-[1000px] w-full">
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
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Formula</FormLabel>
                      <FormControl>
                        <div className="border rounded-lg">
                          <Input
                            className="rounded-none border-x-0 border-t-0"
                            {...field}
                          />
                          <div className="p-4 flex flex-row gap-4">
                            <div className="grid grid-cols-4 gap-1 max-w-[300px]">
                              <FormulaButton className="col-span-2">
                                {'('}
                              </FormulaButton>
                              <FormulaButton className="col-span-2">
                                {')'}
                              </FormulaButton>
                              <FormulaButton className="">7</FormulaButton>
                              <FormulaButton className="">8</FormulaButton>
                              <FormulaButton className="">9</FormulaButton>
                              <FormulaButton className="">/</FormulaButton>
                              <FormulaButton className="">4</FormulaButton>
                              <FormulaButton className="">5</FormulaButton>
                              <FormulaButton className="">6</FormulaButton>
                              <FormulaButton className="">*</FormulaButton>
                              <FormulaButton className="">1</FormulaButton>
                              <FormulaButton className="">2</FormulaButton>
                              <FormulaButton className="">3</FormulaButton>
                              <FormulaButton className="">-</FormulaButton>
                              <FormulaButton className="">0</FormulaButton>
                              <FormulaButton className="">.</FormulaButton>
                              <div />
                              <FormulaButton className="">+</FormulaButton>
                            </div>
                            <div className="overflow-y-scroll">
                              asdasdasdasdasdasdsdasdasdasdasdasd
                            </div>
                          </div>
                        </div>
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              {/* {isError && <TextSub className="text-destructive">{error}</TextSub>} */}
              <Button variant="outline">
                <Link to="/admin/console/awards">Cancel</Link>
              </Button>
              <Button type="submit" variant="default">
                Create
              </Button>
            </form>
          </Form>
        </div>
      </Console.Content>
    </Console>
  )
}
