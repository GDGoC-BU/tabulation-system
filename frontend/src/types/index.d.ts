import type JwtPayload from 'jwt-decode'

type ComponentClassNameProp = {
  className?: string
}
type ComponentChildrenProp = {
  children?: React.ReactNode
}
type ComponentClassNameAndChildrenProp = ComponentClassNameProp &
  ComponentChildrenProp

type ServerFormActionResponse = {
  isSuccessful: boolean
  message?: string
}

type BackendJwtPayload = {
  role: string
  account_id: string
  assigned_pageant_id: string | null
} & JwtPayload

type BackendErrorResponse = {
  status: number
  statusPhrase: string
  errorCode: string
  message: string
  path: string
  timestamp: string
}
