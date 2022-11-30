import { CheckCircleIcon } from '@chakra-ui/icons'
import {
  Container,
  Box,
  Heading,
  List,
  ListItem,
  ListIcon
} from '@chakra-ui/react'
import { useEffect, useState } from 'react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'
import Login from '../components/login'
import SimpleNavBar from '../components/simple-navbar'
import auth from '../utils/auth'

const HomePage = () => {
  const [validated, setValidated] = useState(false)

  useEffect(() => {
    auth.isAuth().then(t => {
      setValidated(t)
    })
  }, [validated])

  return (
    <Box>
      {validated ? (
        <Box pb={4} minHeight="100vh" position="relative">
          <NavBar />
          <Container maxW={'container.sm'} pt="8rem" pb={700}>
            <Heading paddingBottom={10}>Video Streaming App</Heading>
            <p>
              Prototype for uploading and watching videos directly from AWS S3
            </p>
            <List spacing={3}>
              <ListItem>
                <ListIcon as={CheckCircleIcon} color="green.500" />
                Upload MP4 videos to S3
              </ListItem>
              <ListItem>
                <ListIcon as={CheckCircleIcon} color="green.500" />
                Stores data in AWS S3
              </ListItem>
              <ListItem>
                <ListIcon as={CheckCircleIcon} color="green.500" />
                View videos directly from S3
              </ListItem>
            </List>
          </Container>
          <Footer />
        </Box>
      ) : (
        <Box>
          <SimpleNavBar />
          <Box
            position="fixed"
            top="50%"
            left="50%"
            marginTop="-150px"
            marginLeft="-250px"
            textAlign={'center'}
          >
            <Box marginBottom={'2rem'}>
              <Heading as="h1">Video Streaming Prototype</Heading>
            </Box>
            <Login />
          </Box>
        </Box>
      )}
    </Box>
  )
}

export default HomePage
