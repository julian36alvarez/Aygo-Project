import {
  Container,
  Box,
  Heading,
  FormControl,
  FormLabel,
  Input,
  Button
} from '@chakra-ui/react'
import { useEffect, useState } from 'react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'
import auth from '../utils/auth'
import config from '../config/config'
import HomePage from '.'

const UploadVideo = () => {
  const [description, setDescription] = useState('')
  const [file, setFile] = useState(null)
  const [validated, setValidated] = useState(false)
  const [tmp, setTmp] = useState(1)

  useEffect(() => {
    auth.isAuth().then(t => {
      setValidated(t)
    })
  }, [])

  const handleDescriptionChange = e => setDescription(e.target.value)
  const handleFileChange = e => setFile(e.target.files[0])

  const sendVideo = e => {
    e.preventDefault()
    if (description === '' || file === null) {
      alert('Please fill out all fields')
      return
    }
    const formData = new FormData()
    formData.append('file', file)
    fetch(`${config.url}/fileupload?description=${description}`, {
      method: 'POST',
      body: formData
    })
      .then(r => {
        setDescription('')
        setTmp(tmp ^ 1)
        alert('Video uploaded successfully')
      })
      .catch(e => {
        alert('Error uploading video')
      })
  }

  return (
    <Box>
      {validated ? (
        <Box pb={4}>
          <NavBar />
          <Container maxW={'container.md'} pt={20}>
            <Heading>Video Streaming from S3</Heading>
            <Container
              paddingTop={10}
              maxW={'container.sm'}
              paddingBottom={'800px'}
            >
              <FormControl>
                <FormLabel>Video Description</FormLabel>
                <Input
                  type={'text'}
                  name="description"
                  marginBottom={8}
                  onChange={handleDescriptionChange}
                  value={description}
                />
                <Input
                  key={tmp}
                  id="file"
                  type="file"
                  variant="unstyled"
                  onChange={handleFileChange}
                />
                <Button
                  type="submit"
                  mt="4"
                  colorScheme={'green'}
                  onClick={sendVideo}
                >
                  Upload
                </Button>
              </FormControl>
            </Container>
          </Container>
          <Footer />
        </Box>
      ) : (
        <HomePage />
      )}
    </Box>
  )
}

export default UploadVideo
