import {
  Box,
  Container,
  Flex,
  Stack,
  HStack,
  useColorModeValue,
  Link,
  IconButton,
  useDisclosure,
  useColorMode
} from '@chakra-ui/react'
import { CloseIcon, HamburgerIcon } from '@chakra-ui/icons'
import NextLink, { LinkProps } from 'next/link'
import { useRouter } from 'next/router'
import { RiGithubLine } from 'react-icons/ri'
import { animated, config, useSpring } from 'react-spring'
import ThemeButton from './theme-button'
import { useState } from 'react'
import theme from '../themes/theme'
import HomeLink from './home-link'

type TLink = {
  name: string
  path: string
}

const Links: TLink[] = [
  { name: 'Upload Videos', path: '/upload' },
  { name: 'Watch Videos', path: '/watch' }
]

const AnimatedLink = animated(Link)

type Props = {
  href: string
  routerPath: string
  children: React.ReactChild | React.ReactChild[]
  props?: LinkProps
}

const NavBarLink = ({ href, routerPath, children, ...props }: Props) => {
  const isActive = href == routerPath
  const lightTheme = [theme.colors.black, theme.colors.gray['500']]
  const darkTheme = [theme.colors.white, theme.colors.gray['500']]
  const { colorMode, toggleColorMode } = useColorMode()
  const [isHovered, setIsHovered] = useState(false)
  const activeColor = (
    isHovered: boolean,
    isActive: boolean,
    colorMode: string
  ) => {
    const idx = isHovered || isActive ? 0 : 1
    return colorMode === 'light' ? lightTheme[idx] : darkTheme[idx]
  }
  const animatedValues = useSpring({
    color: activeColor(isHovered, isActive, colorMode),
    config: config.gentle
  })

  return (
    <NextLink href={href} passHref scroll={false}>
      <AnimatedLink
        onMouseOver={() => setIsHovered(true)}
        onMouseOut={() => setIsHovered(false)}
        px={2}
        rounded={'md'}
        display={'inline-flex'}
        alignItems={'center'}
        _hover={{
          textDecoration: 'none'
        }}
        _focus={{ boxShadow: 'none' }}
        style={animatedValues}
        {...props}
      >
        {children}
      </AnimatedLink>
    </NextLink>
  )
}

const NavBar = () => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const cpath = useRouter().asPath
  return (
    <Box
      as={'nav'}
      pos={'fixed'}
      width={'100%'}
      bg={useColorModeValue(
        'rgba(226, 232, 240, 0.9)',
        'rgba(23, 25, 35, 0.9)'
      )}
    >
      <Container maxW={'container.xl'} justifyContent={'space-between'}>
        <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
          <IconButton
            size={'md'}
            icon={isOpen ? <CloseIcon /> : <HamburgerIcon />}
            aria-label={'Open Menu'}
            display={{ md: 'none' }}
            onClick={isOpen ? onClose : onOpen}
          />
          <HStack spacing={8} alignItems={'center'}>
            <HomeLink>Video Stream &lt; S3</HomeLink>
            <HStack
              as={'nav'}
              spacing={4}
              display={{ base: 'none', md: 'flex' }}
            >
              {Links.map(link => (
                <NavBarLink key={link.name} href={link.path} routerPath={cpath}>
                  {link.name}
                </NavBarLink>
              ))}
            </HStack>
          </HStack>
          <Flex alignItems={'center'}>
            <Link
              _focus={{ boxShadow: 'none' }}
              display={'inline-flex'}
              href={'https://www.github.com/julian36alvarez/Aygo-Project'}
            >
              <RiGithubLine size={20} />
              Source
            </Link>
            <ThemeButton />
          </Flex>
        </Flex>
        {isOpen ? (
          <Box pb={4} display={{ md: 'none' }}>
            <Stack as={'nav'} spacing={4}>
              {Links.map(link => (
                <NavBarLink key={link.name} href={link.path} routerPath={cpath}>
                  {link.name}
                </NavBarLink>
              ))}
            </Stack>
          </Box>
        ) : null}
      </Container>
    </Box>
  )
}

export default NavBar
