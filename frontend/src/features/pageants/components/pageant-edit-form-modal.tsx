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
import { Ellipsis } from 'lucide-react'
import { PageantSummary } from '../schemas/pageant'
import { useState } from 'react'
import { ServerFormActionResponse } from '@/types'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import z from 'zod'
import { pageantEditSchema } from '../schemas/pageant-edit'
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
import { TextBody, TextSub } from '@/components/text'
import { Button } from '@/components/ui/button'
import { DialogClose } from '@radix-ui/react-dialog'
import { editPageant } from '../actions/edit-pageant'

export default function PageantEditFormModal({
  pageant
}: {
  pageant: PageantSummary
}) {
  const [isLoading, setIsLoading] = useState(false)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [error, setError] = useState<ServerFormActionResponse | null>(null)
  const router = useRouter()

  const { id, title } = pageant

  const form = useForm<z.infer<typeof pageantEditSchema>>({
    resolver: zodResolver(pageantEditSchema),
    defaultValues: {
      id: id,
      title: title
    }
  })

  async function onSubmit(values: z.infer<typeof pageantEditSchema>) {
    setIsLoading(true)
    const response = await editPageant(values)
    if (response.isSuccessful) {
      router.refresh()
    } else {
      setError(response)
    }
    console.log('Values: ', values)
    setIsLoading(false)
    setIsDialogOpen(false)
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
          <form onSubmit={form.handleSubmit(onSubmit)} className='space-y-4'>
            <DialogHeader>
              <DialogTitle>
                <TextBody>Edit Pageant</TextBody>
              </DialogTitle>
              <DialogDescription asChild>
                <TextSub>
                  {
                    "Make changes to pageant details here. Click save when you're done."
                  }
                </TextSub>
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
                <TextBody className='text-background'>Save changes</TextBody>
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
