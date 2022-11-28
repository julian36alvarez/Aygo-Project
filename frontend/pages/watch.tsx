import { ChevronRightIcon } from '@chakra-ui/icons'
import {
  Container,
  Box,
  Heading,
  ListItem,
  List,
  Grid,
  GridItem,
  ListIcon,
  Flex,
  useColorMode
} from '@chakra-ui/react'
import { useEffect, useState } from 'react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'
import styled from '@emotion/styled'
import config from '../config/config'

const WatchVideo = () => {
  const { colorMode, toggleColorMode } = useColorMode()
  let backgroundColor = '#f5f5f5'
  if (colorMode === 'dark') {
    backgroundColor = '#232c8c'
  }

  const StyledDiv = styled.div`
    &:hover {
      cursor: pointer;
      background-color: ${backgroundColor};
    }
  `
  const [videos, setVideos] = useState([])
  const [currName, setCurrName] = useState(null)

  useEffect(() => {
    fetch(`${config.url}/items`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(response => {
      response.json().then(data => {
        setVideos(data)
      })
    })
  }, [])

  const handleVideoClick = v => {
    setCurrName(v.name)
  }

  const getVideos = () => {
    return videos.map(v => {
      return (
        <StyledDiv
          onClick={e => {
            e.preventDefault()
            handleVideoClick(v)
          }}
        >
          <ListItem pb={4} paddingLeft={2}>
            <Heading as="h5" size={'lg'}>
              {v.name}
            </Heading>
            <p>{v.desc}</p>
          </ListItem>
        </StyledDiv>
      )
    })
  }

  const getUrl = () => {
    if (currName) {
      return `${config.url}/${currName}/stream`
    }
  }

  return (
    <Box pb={4}>
      <NavBar />
      <Container maxW={'container.xl'} pt={20}>
        <Heading>Video Streaming from S3</Heading>
        <Grid paddingBottom={'800px'} templateColumns="repeat(2, 55fr)" gap={2}>
          <GridItem>
            <Container paddingTop={10} maxW={'container.sm'}>
              <video
                id="video1"
                width="750"
                height="440"
                controls
                src={getUrl()}
              >
                <source type="video/mp4" />
              </video>
            </Container>
          </GridItem>
          <GridItem>
            <Box overflowY={'scroll'} maxH="350px">
              <List>{getVideos()}</List>
            </Box>
          </GridItem>
        </Grid>
      </Container>
      <Footer />
    </Box>
  )
}

export default WatchVideo
