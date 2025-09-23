'use client'

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuBadge,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarSeparator
} from '@/components/ui/sidebar'
import {
  Circle,
  Crown,
  Gauge,
  Layers,
  Route,
  School,
  UserRoundPen,
  UserStar
} from 'lucide-react'
import Link from 'next/link'
import { TextBody, TextSub } from '@/components/text'
import { usePageant } from '@/features/pageants/store/usePageant'

const pageantAction = [
  {
    title: 'Judges',
    url: '/admin/console/judges',
    icon: UserRoundPen,
    badgeValue: 4
  },
  {
    title: 'Candidates',
    url: '/admin/console/candidates',
    icon: UserStar,
    badgeValue: 20
  },
  {
    title: 'Phases',
    url: '/admin/console/phases',
    icon: Layers,
    badgeValue: 2
  },
  {
    title: 'Segments',
    url: '/admin/console/segments',
    icon: Route,
    badgeValue: 5
  }
]

export default function Vertical() {
  const pageant = usePageant(state => state.pageant)

  const currentPageant = pageant ? pageant.title : '-'

  return (
    <Sidebar variant='sidebar' collapsible='icon'>
      <SidebarHeader className='flex h-[65px] flex-col justify-center border-b'>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton className='h-full hover:cursor-pointer' asChild>
              <Link href='/admin/console'>
                <Circle />
                <TextBody className='font-bold'>Tabulation</TextBody>
              </Link>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>

      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Managing</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuButton>{currentPageant}</SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroup>
          <SidebarGroupLabel>Home</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/pageants'>
                  <Crown />
                  Pageants
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/dashboard'>
                  <Gauge />
                  Dashboard
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupLabel>Manage</SidebarGroupLabel>
          <SidebarGroupContent>
            {pageantAction.map((action, index) => {
              return (
                <SidebarMenu key={index}>
                  <SidebarMenuItem>
                    <SidebarMenuButton asChild>
                      <Link href={action.url}>
                        <action.icon />
                        {action.title}
                      </Link>
                    </SidebarMenuButton>
                    <SidebarMenuBadge>{action.badgeValue}</SidebarMenuBadge>
                  </SidebarMenuItem>
                </SidebarMenu>
              )
            })}
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarSeparator />

        <SidebarGroup>
          <SidebarGroupLabel>Globals</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/colleges'>
                  <School />
                  Colleges
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}
