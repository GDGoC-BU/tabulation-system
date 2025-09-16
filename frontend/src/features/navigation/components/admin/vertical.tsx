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
  return (
    <Sidebar variant='sidebar' collapsible='icon'>
      <SidebarHeader className='flex h-[65px] flex-col justify-center border-b'>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton className='h-full hover:cursor-pointer' asChild>
              <Link href='/admin/console'>
                <Circle />
                <div className='flex flex-col'>
                  <TextSub>Managing</TextSub>
                  <TextBody className='font-bold'>MMBU 2025</TextBody>
                </div>
              </Link>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>

      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>
            <TextSub>Home</TextSub>
          </SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/pageants'>
                  <Crown />
                  <TextBody>Pageants</TextBody>
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/dashboard'>
                  <Gauge />
                  <TextBody>Dashboard</TextBody>
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupLabel>
            <TextSub>Manage</TextSub>
          </SidebarGroupLabel>
          <SidebarGroupContent>
            {pageantAction.map((action, index) => {
              return (
                <SidebarMenu key={index}>
                  <SidebarMenuItem>
                    <SidebarMenuButton asChild>
                      <Link href={action.url}>
                        <action.icon />
                        <TextBody>{action.title}</TextBody>
                      </Link>
                    </SidebarMenuButton>
                    <SidebarMenuBadge>
                      <TextSub>{action.badgeValue}</TextSub>
                    </SidebarMenuBadge>
                  </SidebarMenuItem>
                </SidebarMenu>
              )
            })}
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarSeparator />

        <SidebarGroup>
          <SidebarGroupLabel>
            <TextSub>Globals</TextSub>
          </SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuButton asChild>
                <Link href='/admin/console/colleges'>
                  <School />
                  <TextBody>Colleges</TextBody>
                </Link>
              </SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}
