import { createFileRoute, redirect, useNavigate } from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import type { LoginParameters } from '@/features/authentication/schemas'
import { loginSchema } from '@/features/authentication/schemas'
import { TextBody, TextHeading, TextSub } from '@/components/text'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { useLoginMutation } from '@/features/authentication/hooks/use-login-mutation'
import { useAuthenticationStore } from '@/features/authentication/store/use-authentication-store'
import { waitForStoreHydration } from '@/lib/wait-for-store-hydration'

export const Route = createFileRoute('/judge/login/')({
  validateSearch: (search) => ({
    redirect: (search.redirect as string) || '/judge/scoring',
  }),
  beforeLoad: async ({ context, search }) => {
    /* Wait for zustand to load from locale storage */
    await waitForStoreHydration(useAuthenticationStore)
    /* Redirect if already authenticated */
    const isAuthenticated = context.authentication.isAuthenticated()
    if (isAuthenticated) {
      throw redirect({ to: search.redirect })
    }
  },
  component: JudgeLogin,
})

function JudgeLogin() {
  const { authentication } = Route.useRouteContext()
  const { redirect: redirectBack } = Route.useSearch()
  const { mutateAsync, isError, error, isPending } = useLoginMutation()
  const navigate = useNavigate()

  async function onSubmit(values: LoginParameters) {
    const token = await mutateAsync(values)
    authentication.login(token)
    navigate({ to: redirectBack })
  }

  const form = useForm<LoginParameters>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: '',
      password: '',
    },
  })
  return (
    <div className="relative h-screen select-none">
      <div className="absolute inset-0">
        <img
          src="/images/judge-login-bg.png"
          className="object-contain w-full"
          alt="Judge Login Background"
        />
      </div>
      <div className="absolute top-[50%] translate-x-[50%] translate-y-[-50%] right-[50%] size-fit flex flex-col items-center gap-8 rounded-xl border px-8 py-8 bg-background">
        <div className="flex flex-col gap-2 items-center">
          <div className="size-[100px] grid place-items-center">
            <img
              src="/images/mmbu-logo.png"
              className="object-contain w-full"
              alt="Judge Login Background"
            />
          </div>
          <div className="flex flex-col gap-1 items-center">
            <TextHeading>Mr. and Ms. Bicol University 2025</TextHeading>
            <TextBody>Judge Login</TextBody>
          </div>
        </div>
        <div className="flex flex-col items-center gap-2">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="w-fit flex flex-col gap-4"
            >
              <div className="flex flex-row gap-4">
                <FormField
                  control={form.control}
                  name="username"
                  render={({ field }) => (
                    <FormItem>
                      <FormControl>
                        <Input {...field} placeholder="Username" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem>
                      <FormControl>
                        <Input
                          type="password"
                          {...field}
                          placeholder="Password"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              {isError && (
                <TextSub className="text-destructive self-center">
                  {error}
                </TextSub>
              )}
              <Button disabled={isPending} variant="outline" className="w-full">
                Start Scoring
              </Button>
            </form>
          </Form>
        </div>
      </div>
    </div>
  )
}
