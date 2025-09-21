'use client'

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { useForm } from 'react-hook-form'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { Button } from '@/components/ui/button'
import { TextBody, TextSub } from '@/components/text'
import loginSchema from '../schemas/login'
import { login } from '../actions/login'
import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function Admin() {
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<ServerFormActionResponse | null>(null)
  const router = useRouter()

  const form = useForm<z.infer<typeof loginSchema>>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: '',
      password: ''
    }
  })

  async function onSubmit(values: z.infer<typeof loginSchema>) {
    setIsLoading(true)
    const response = await login(values)
    if (response.isSuccessful) {
      router.push('/admin/console')
    } else {
      setError(response)
    }
    setIsLoading(false)
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className='w-[250px] space-y-4'
      >
        <FormField
          control={form.control}
          name='username'
          render={({ field }) => (
            <FormItem>
              <FormLabel>Username</FormLabel>
              <FormControl>
                <Input placeholder='admin' {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name='password'
          render={({ field }) => (
            <FormItem>
              <FormLabel>Password</FormLabel>
              <FormControl>
                <Input type='password' placeholder='123' {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        {error && (
          <TextSub className='text-destructive'>{error.message}</TextSub>
        )}
        <Button disabled={isLoading} variant='outline' className='w-full'>
          <TextBody>Submit</TextBody>
        </Button>
      </form>
    </Form>
  )
}
