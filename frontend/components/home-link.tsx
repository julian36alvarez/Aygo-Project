import { Link } from '@chakra-ui/react'
import React from 'react'
import { RiVideoFill } from 'react-icons/ri'
import NextLink from 'next/link'
import styled from '@emotion/styled'

const StyledBox = styled.div`
  align-items: center;
  display: inline-flex;

  svg {
    margin-right: 4px;
    transition: 180ms ease;
  }

  &:hover svg {
    transform: rotate(-15deg);
  }
`

const HomeLink = ({ children }: { children: React.ReactChild }) => (
  <NextLink href={'/'} passHref>
    <Link _focus={{ boxShadow: 'none' }}>
      <StyledBox>
        <RiVideoFill />
        {children}
      </StyledBox>
    </Link>
  </NextLink>
)

export default HomeLink
