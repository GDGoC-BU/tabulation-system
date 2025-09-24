'use client'

import {
  Dialog,
  DialogHeader,
  DialogContent,
  DialogDescription,
  DialogTitle,
  DialogTrigger,
  DialogFooter
} from '@/components/ui/dialog'
import { useState } from 'react'
import { ServerFormActionResponse } from '@/types'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import z from 'zod'
import { pageantAddSchema } from '../schemas/pageant-add'
import { zodResolver } from '@hookform/resolvers/zod'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { TextSub } from '@/components/text'
import { Button } from '@/components/ui/button'
import { DialogClose } from '@radix-ui/react-dialog'
import { addPageant } from '../actions/add-pageant'

export default function PageantAddFormDialog() {
  const [isLoading, setIsLoading] = useState(false)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [error, setError] = useState<ServerFormActionResponse | null>(null)
  const router = useRouter()

  const form = useForm<z.infer<typeof pageantAddSchema>>({
    resolver: zodResolver(pageantAddSchema),
    defaultValues: {
      title: ''
    }
  })

  async function onSubmit(values: z.infer<typeof pageantAddSchema>) {
    setIsLoading(true)
    const response = await addPageant(values)
    if (response.isSuccessful) {
      router.refresh()
      form.reset()
      form.clearErrors()
      setError(null)
      setIsDialogOpen(false)
    } else {
      setError(response)
    }
    setIsLoading(false)
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
          <form onSubmit={form.handleSubmit(onSubmit)} className='space-y-4'>
            <DialogHeader>
              <DialogTitle>Add Pageant</DialogTitle>
              <DialogDescription>
                Add a pageant here (CHANGE THIS DESCRIPTION LOL)
              </DialogDescription>
            </DialogHeader>
            <FormField
              control={form.control}
              name='title'
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
            {error && (
              <TextSub className='text-destructive'>{error.message}</TextSub>
            )}
            <DialogFooter>
              <DialogClose asChild>
                <Button variant='outline'>Cancel</Button>
              </DialogClose>
              <Button type='submit' disabled={isLoading} variant='default'>
                Create
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
