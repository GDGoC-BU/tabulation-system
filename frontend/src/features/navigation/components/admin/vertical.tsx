import {
  Circle,
  Columns3Cog,
  Crown,
  Gauge,
  Layers,
  Route,
  School,
  UserRoundPen,
  UserStar,
} from 'lucide-react'
import { Link } from '@tanstack/react-router'
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
  SidebarSeparator,
} from '@/components/ui/sidebar'
import { TextBody } from '@/components/text'
import { useSelectedPageant } from '@/features/pageants/hooks/use-selected-pageant'

const pageantAction = [
  {
    title: 'Judges',
    url: '/admin/console/judges',
    icon: UserRoundPen,
    badgeValue: 0,
  },
  {
    title: 'Candidates',
    url: '/admin/console/candidates',
    icon: UserStar,
    badgeValue: 0,
  },
  {
    title: 'Phases',
    url: '/admin/console/phases',
    icon: Layers,
    badgeValue: 0,
  },
  {
    title: 'Segments',
    url: '/admin/console/segments',
    icon: Route,
    badgeValue: 0,
  },
  {
    title: 'Awards',
    url: '/admin/console/awards',
    icon: Route,
    badgeValue: 0,
  },
  {
    title: 'Blockly',
    url: '/admin/console/blockly',
    icon: Route,
    badgeValue: 0,
  },
]

export default function Vertical() {
  const { data } = useSelectedPageant()

  const showManagePageantLinks = true
  // !data || data.status.value !== pageantStatusValue.enum.PREPARATION

  return (
    <Sidebar variant="sidebar" collapsible="icon">
      <SidebarHeader className="flex h-[65px] flex-col justify-center border-b">
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton className="h-full hover:cursor-pointer" asChild>
              <Link to="/admin/console">
                <Circle />
                <TextBody className="font-bold">Tabulation</TextBody>
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
              <SidebarMenuButton>
                <Columns3Cog />
                {data ? data.title : '-'}
              </SidebarMenuButton>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroup>
          <SidebarGroupLabel>Home</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
                  <Link to="/admin/console/pageants">
                    <Crown />
                    Pageants
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
            <SidebarMenu>
              <SidebarMenuItem
                className={!data ? 'pointer-events-none opacity-50' : ''}
              >
                <SidebarMenuButton asChild>
                  <Link to="/admin/console/dashboard">
                    <Gauge />
                    Dashboard
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupLabel>Manage</SidebarGroupLabel>
          <SidebarGroupContent>
            {pageantAction.map((action, index) => {
              return (
                <SidebarMenu key={index}>
                  <SidebarMenuItem
                    className={
                      showManagePageantLinks
                        ? ''
                        : 'pointer-events-none opacity-50'
                    }
                  >
                    <SidebarMenuButton asChild>
                      <Link to={action.url}>
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
                <Link to="/admin/console/colleges">
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
