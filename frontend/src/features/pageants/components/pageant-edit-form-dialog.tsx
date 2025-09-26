import { Ellipsis } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { DialogClose } from '@radix-ui/react-dialog'
import { useQueryClient } from '@tanstack/react-query'
import { pageantEditSchema } from '../schemas'
import useEditPageantMutate from '../hooks/use-edit-pageant-mutate'
import useDeletePageantMutate from '../hooks/use-delete-pageant-mutate'
import type { PageantEditParameters, PageantSummary } from '../schemas'
import { Button } from '@/components/ui/button'
import { TextSub } from '@/components/text'
import { Input } from '@/components/ui/input'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'

export default function PageantEditFormDialog({
  pageant,
}: {
  pageant: PageantSummary
}) {
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const {
    mutateAsync: editPageant,
    isPending: editIsPending,
    isError: editIsError,
    error: editError,
  } = useEditPageantMutate()
  const {
    mutateAsync: deletePageant,
    isPending: deleteIsPending,
    isError: deleteIsError,
    error: deleteError,
  } = useDeletePageantMutate()
  const queryClient = useQueryClient()

  const form = useForm<PageantEditParameters>({
    resolver: zodResolver(pageantEditSchema),
    defaultValues: {
      id: '',
      title: '',
    },
  })

  /* form default values are only set on initial render,
     Reset the default values again on open to get the
     fresh pageant details */
  useEffect(() => {
    if (isDialogOpen) {
      form.reset({
        id: pageant.id,
        title: pageant.title,
      })
    }
  }, [isDialogOpen, pageant, form])

  async function onEditSubmit(values: PageantEditParameters) {
    const isSuccess = await editPageant(values)
    if (isSuccess) {
      queryClient.invalidateQueries({ queryKey: ['pageants'] })
      form.clearErrors()
      setIsDialogOpen(false)
    }
  }

  async function onDeleteSubmit() {
    const values = form.getValues()
    const isSuccess = await deletePageant({ id: values.id })
    if (isSuccess) {
      queryClient.invalidateQueries({ queryKey: ['pageants'] })
      form.clearErrors()
      setIsDialogOpen(false)
    }
  }

  const onOpenChange = (open: boolean) => {
    setIsDialogOpen(open)
  }

  return (
    <Dialog open={isDialogOpen} onOpenChange={onOpenChange}>
      <DialogTrigger>
        <Ellipsis />
      </DialogTrigger>
      <DialogContent>
        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onEditSubmit)}
            className="space-y-4"
          >
            <DialogHeader>
              <DialogTitle>Edit Pageant</DialogTitle>
              <DialogDescription>
                {
                  "Make changes to pageant details here. Click save when you're done."
                }
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
            {editIsError && (
              <TextSub className="text-destructive">{editError}</TextSub>
            )}
            {deleteIsError && (
              <TextSub className="text-destructive">{deleteError}</TextSub>
            )}
            <DialogFooter>
              <DialogClose asChild>
                <Button variant="outline">Cancel</Button>
              </DialogClose>
              <Button
                onClick={onDeleteSubmit}
                type="button"
                disabled={editIsPending || deleteIsPending}
                variant="destructive"
              >
                Delete
              </Button>
              <Button
                type="submit"
                disabled={editIsPending || deleteIsPending}
                variant="default"
              >
                Save changes
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
