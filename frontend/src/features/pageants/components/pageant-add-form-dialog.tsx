import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { DialogClose } from '@radix-ui/react-dialog'
import { useQueryClient } from '@tanstack/react-query'
import { pageantAddSchema } from '../schemas'
import useAddPageantMutate from '../hooks/use-add-pageant-mutate'
import type { PageantAddParameters } from '../schemas'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { TextSub } from '@/components/text'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'

export default function PageantAddFormDialog() {
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const {
    mutateAsync: addPageant,
    isPending,
    isError,
    error,
  } = useAddPageantMutate()
  const queryClient = useQueryClient()

  const form = useForm<PageantAddParameters>({
    resolver: zodResolver(pageantAddSchema),
    defaultValues: {
      title: '',
    },
  })

  async function onSubmit(values: PageantAddParameters) {
    const isSuccess = await addPageant(values)
    if (isSuccess) {
      queryClient.invalidateQueries({ queryKey: ['pageants'] })
      form.reset()
      form.clearErrors()
      setIsDialogOpen(false)
    }
  }

  const onOpenChange = (open: boolean) => {
    setIsDialogOpen(open)
  }

  return (
    <Dialog open={isDialogOpen} onOpenChange={onOpenChange}>
      <DialogTrigger asChild>
        <Button>Add Pageant</Button>
      </DialogTrigger>
      <DialogContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <DialogHeader>
              <DialogTitle>Add Pageant</DialogTitle>
              <DialogDescription>
                Add a pageant here (CHANGE THIS DESCRIPTION LOL)
              </DialogDescription>
            </DialogHeader>
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Title</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            {isError && <TextSub className="text-destructive">{error}</TextSub>}
            <DialogFooter>
              <DialogClose asChild>
                <Button variant="outline">Cancel</Button>
              </DialogClose>
              <Button type="submit" disabled={isPending} variant="default">
                Create
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
