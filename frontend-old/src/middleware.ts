import { cookies } from 'next/headers'
import { jwtDecode } from 'jwt-decode'
import { NextRequest, NextResponse } from 'next/server'
import { BackendJwtPayload } from './types'

export async function middleware(request: NextRequest) {
  const cookieStore = await cookies()
  const token = cookieStore.get('TOKEN')
  let accountRole: string = ''

  const url = request.nextUrl.clone()

  if (token) {
    const { role } = jwtDecode<BackendJwtPayload>(token.value)
    accountRole = role
  }

  if (
    url.pathname.startsWith('/admin') &&
    (!token || accountRole !== 'ADMIN')
  ) {
    url.pathname = '/admin/login'
    return NextResponse.redirect(url)
  }

  if (
    url.pathname.startsWith('/judge') &&
    (!token || accountRole !== 'JUDGE')
  ) {
    url.pathname = '/judge/login'
    return NextResponse.redirect(url)
  }

  return NextResponse.next()
}

export const config = {
  matcher: ['/admin/console/:path*']
}
