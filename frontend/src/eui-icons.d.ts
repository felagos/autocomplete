declare module '@elastic/eui/es/components/icon/assets/*' {
  import { ComponentType, SVGProps } from 'react'
  export const icon: ComponentType<SVGProps<SVGSVGElement>>
}

declare module '@elastic/eui/es/components/icon/icon' {
  import { ComponentType } from 'react'
  export function appendIconComponentCache(iconTypeToIconComponentMap: {
    [iconType: string]: ComponentType
  }): void
}
