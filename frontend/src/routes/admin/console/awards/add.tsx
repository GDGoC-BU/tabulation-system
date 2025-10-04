import { Link, createFileRoute, useNavigate } from '@tanstack/react-router'
import { useMemo, useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
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
import { usePageantHierarchyQuery } from '@/features/pageants/hooks/use-pageant-hierarchy'
import renderFormulaLabel from '@/features/formula/components/lib/render-formula-label'
import deleteLastFormulaChunk from '@/features/formula/components/lib/delete-last-formula-chunk'
import { Textarea } from '@/components/ui/textarea'

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

  const { data: selectedPageant } = useSelectedPageantQuery()
  const { data: pageantHierarchy } = usePageantHierarchyQuery(
    selectedPageant?.id,
  )
  const navigate = useNavigate()

  const form = useForm({
    resolver: zodResolver(awardAddFormSchema),
    defaultValues: {
      name: '',
      candidateLimit: '' as unknown as number,
      formula: '',
    },
  })

  const criteriaButtonItems = useMemo(() => {
    if (!pageantHierarchy) return []
    const buttonItems: Array<ButtonItem> = []
    pageantHierarchy.phases.forEach((phase) => {
      phase.segments.forEach((segment) => {
        segment.criteria.forEach((criterion) => {
          const item: ButtonItem = {
            className: '',
            label: `${phase.name} : ${segment.name} : ${criterion.name}`,
            value: criterion.id,
          }
          buttonItems.push(item)
        })
      })
    })
    return buttonItems
  }, [pageantHierarchy])

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

  const handleButtonInput = (button: ButtonItem) => {
    if (!button.value) return

    const updatedFormula = rawFormula + button.value
    setRawFormula(updatedFormula)
    form.setValue('formula', updatedFormula)
  }

  const handleButtonDelete = () => {
    const newRawFormula = deleteLastFormulaChunk(rawFormula)
    setRawFormula(newRawFormula)
    form.setValue('formula', newRawFormula)
  }

  function onSubmit(values: AwardAddForm) {
    console.log('Award Values: ', values)
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
        <div className="w-fit">
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
                        <FormControl>
                          <Textarea
                            value={renderFormulaLabel(rawFormula, criterionMap)}
                            className="rounded-none border-x-0 border-t-0"
                            // readOnly
                          />
                        </FormControl>
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
                                  onClick={() => handleButtonInput(button)}
                                >
                                  {button.label}
                                </FormulaButton>
                              )
                            })}
                          </div>
                          <div className="overflow-y-scroll gap-1 flex flex-col h-[235px]">
                            {criteriaButtonItems.map((button, index) => {
                              return (
                                <FormulaButton
                                  className={
                                    button.className ? button.className : ''
                                  }
                                  key={index}
                                  onClick={() => handleButtonInput(button)}
                                >
                                  {button.label}
                                </FormulaButton>
                              )
                            })}
                          </div>
                        </div>
                      </div>
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
