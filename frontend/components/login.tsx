import { Button } from '@chakra-ui/react'
import config from '../config/config'
import { useRouter } from 'next/router'

const Login = () => {
  const router = useRouter()
  const url = `https://${config.cognito_hosted_domain}/login?response_type=token&client_id=${config.cognito_client_id}&redirect_uri=${config.redirect_url}`
  return (
    <Button
      variant={'solid'}
      color="green.500"
      onClick={() => router.push(url)}
    >
      Access
    </Button>
  )
}

export default Login
