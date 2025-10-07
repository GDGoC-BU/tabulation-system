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
import FormulaButton from '@/features/formula/components/formula-button'
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import deleteLastFormulaChunk from '@/features/formula/lib/delete-last-formula-chunk'
import useAddAwardMutate from '@/features/awards/hooks/use-add-award-mutate'
import useFormulaCriterionLookup from '@/features/formula/hooks/use-formula-criterion-lookup'
import FormulaBadgeRenderer from '@/features/formula/components/formula-label-renderer'
import FormulaRenderer from '@/features/formula/components/formula-renderer'

type ButtonItem = {
  className: string | null
  label: string
  value: string | null
}

const mainButtonItems: Array<ButtonItem> = [
  { className: 'col-span-2', label: '(', value: '(' },
  { className: 'col-span-2', label: ')', value: ')' },
  { className: '', label: '7', value: '7' },
  { className: '', label: '8', value: '8' },
  { className: '', label: '9', value: '9' },
  { className: '', label: '/', value: '/' },
  { className: '', label: '4', value: '4' },
  { className: '', label: '5', value: '5' },
  { className: '', label: '6', value: '6' },
  { className: '', label: '*', value: '*' },
  { className: '', label: '1', value: '1' },
  { className: '', label: '2', value: '2' },
  { className: '', label: '3', value: '3' },
  { className: '', label: '-', value: '-' },
  { className: '', label: '0', value: '0' },
  { className: '', label: '.', value: '.' },
  { className: 'display-hidden', label: '', value: null },
  { className: '', label: '+', value: '+' },
]

export const Route = createFileRoute('/admin/console/awards/add')({
  component: AdminConsoleAwardsAdd,
})

function AdminConsoleAwardsAdd() {
  const [rawFormula, setRawFormula] = useState('')
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { mutateAsync: addAward } = useAddAwardMutate()
  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )
  const criterionLookup = useFormulaCriterionLookup(
    pageantHierarchy?.phases ?? [],
  )

  const form = useForm({
    resolver: zodResolver(awardAddFormSchema),
    defaultValues: {
      name: '',
      candidateLimit: '' as unknown as number,
      formula: '',
    },
  })

  if (!selectedPageant) {
    navigate({
      to: '/admin/console/pageants',
    })
  }

  const criterionMap = new Map<string, string>()
  pageantHierarchy?.phases.forEach((phase) => {
    phase.segments.forEach((segment) => {
      segment.criteria.forEach((criterion) => {
        const key = criterion.id
        const value = `<${phase.name} : ${segment.name} : ${criterion.name}>`
        criterionMap.set(key, value)
      })
    })
  })

  const handleButtonInput = (value: string | null) => {
    if (!value) return

    const updatedFormula = rawFormula + value
    setRawFormula(updatedFormula)
    form.setValue('formula', updatedFormula)
  }

  const handleButtonDelete = () => {
    const newRawFormula = deleteLastFormulaChunk(rawFormula)
    setRawFormula(newRawFormula)
    form.setValue('formula', newRawFormula)
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

                      <div className="border rounded-lg">
                        <div className="p-4 min-h-[150px] overflow-x-scroll border-b">
                          <FormulaRenderer
                            formula={rawFormula}
                            criterionLookup={criterionLookup}
                          />
                        </div>
                        <FormMessage className="px-4 pt-2" />
                        <div className="p-4 flex flex-row gap-4">
                          <div className="grid grid-cols-4 gap-1">
                            <div className="col-span-4 grid grid-cols-3 gap-1">
                              <FormulaButton disabled={true}>
                                {'<-'}
                              </FormulaButton>
                              <FormulaButton disabled={true}>
                                {'->'}
                              </FormulaButton>
                              <FormulaButton onClick={handleButtonDelete}>
                                DEL
                              </FormulaButton>
                            </div>

                            {mainButtonItems.map((button, index) => {
                              return (
                                <FormulaButton
                                  className={
                                    button.className ? button.className : ''
                                  }
                                  key={index}
                                  onClick={() =>
                                    handleButtonInput(button.value)
                                  }
                                >
                                  {button.label}
                                </FormulaButton>
                              )
                            })}
                          </div>
                          <div className="overflow-y-scroll gap-1 flex flex-col h-[235px]">
                            {Object.values(criterionLookup).map(
                              (criterionRelationship) => {
                                return (
                                  <FormulaButton
                                    key={criterionRelationship.criterion.id}
                                    onClick={() =>
                                      handleButtonInput(
                                        criterionRelationship.criterion.id,
                                      )
                                    }
                                  >
                                    <FormulaBadgeRenderer
                                      simplified={false}
                                      criterionRelationship={
                                        criterionRelationship
                                      }
                                    />
                                  </FormulaButton>
                                )
                              },
                            )}
                          </div>
                        </div>
                      </div>
                    </FormItem>
                  )}
                />
              </div>
              {/* {isError && <TextSub className="text-destructive">{error}</TextSub>} */}
              <div className="flex flex-row gap-4">
                <Button variant="outline">
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
