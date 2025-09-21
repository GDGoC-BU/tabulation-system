import { JwtPayload as BaseJwtPayload } from 'jwt-decode'

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

type JwtPayload = {
  role: string
} & JwtPayload

type BackendErrorResponse = {
  status: number
  statusPhrase: string
  errorCode: string
  message: string
  path: string
  timestamp: string
}
