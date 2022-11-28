import { Box, Container, Text } from '@chakra-ui/react'
import NavBar from '../components/navbar'

const Custom404 = () => {
  return (
    <Box display={'block'}>
      <NavBar />
      <Container pt={24} maxW="container.md" textAlign={'center'}>
        <Text fontSize={'6xl'}>404 Not Found</Text>
      </Container>
    </Box>
  )
}

export default Custom404
