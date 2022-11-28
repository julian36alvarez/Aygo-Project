import { CheckCircleIcon } from '@chakra-ui/icons'
import {
  Container,
  Box,
  Heading,
  List,
  ListItem,
  ListIcon
} from '@chakra-ui/react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'

const HomePage = () => {
  return (
    <Box pb={4} minHeight="100vh" position="relative">
      <NavBar />
      <Container maxW={'container.sm'} pt="8rem" pb={700}>
        <Heading paddingBottom={10}>Video Streaming App</Heading>
        <p>Prototype for uploading and watching videos directly from AWS S3</p>
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
  )
}

export default HomePage
