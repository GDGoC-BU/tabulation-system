import {
  Link,
  createFileRoute,
  redirect,
  useNavigate,
} from '@tanstack/react-router'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import type { LoginParameters } from '@/features/authentication/schemas'
import { loginSchema } from '@/features/authentication/schemas'
import { TextHeading, TextSub } from '@/components/text'
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
    <div className="grid h-screen place-items-center">
      <div className="flex flex-col items-center gap-4 rounded-lg border px-8 py-8">
        <div>
          <TextHeading>Judge</TextHeading>
        </div>
        <div className="flex flex-col items-center gap-2">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="w-[250px] space-y-4"
            >
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Username</FormLabel>
                    <FormControl>
                      <Input placeholder="judge_1" {...field} />
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
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <Input type="password" placeholder="1234" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              {isError && (
                <TextSub className="text-destructive">{error}</TextSub>
              )}
              <Button disabled={isPending} variant="outline" className="w-full">
                Login
              </Button>
            </form>
          </Form>
          <Link to="/">Home</Link>
        </div>
      </div>
    </div>
  )
}
