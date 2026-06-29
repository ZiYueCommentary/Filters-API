import {defineConfig} from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    head: [['link', {rel: 'icon', href: '/logo.png'}]],
    title: "Filters API",
    description: "Documentation",
    cleanUrls: true,
    themeConfig: {
        // https://vitepress.dev/reference/default-theme-config
        nav: [
            {text: 'Home', link: '/home'},
            {text: 'Legacy', link: '/legacy'}
        ],
        "logo": "/logo.png",

        sidebar: {
            '/legacy/': {
                base: '',
                items: [
                    {
                        text: 'Legacy',
                        link: '/legacy/',
                        items: [
                            {text: 'Registering filters', link: '/legacy/registering'},
                            {text: 'Adding items', link: '/legacy/adding'},
                            {text: 'Registering uncategorized filter', link: '/legacy/uncategorized'},
                        ]
                    },
                    {
                        text: 'Unsupported',
                        link: '/legacy/unsupported/',
                        items: [
                            {text: 'Sorting filters', link: '/legacy/unsupported/sorting'},
                            {text: 'Removing filters', link: '/legacy/unsupported/removing'}
                        ]
                    }
                ]
            },
            '/': {
                base: '',
                items: [
                    {
                        text: 'Home', link: '/home'
                    },
                    {
                        text: 'Using Filters API',
                        items:
                            [
                                {text: 'Registering filters', link: '/registering'},
                                {text: 'Uncategorized filters', link: '/uncategorized'},
                            ]
                    },
                    {
                        text: 'Advanced',
                        items:
                            [
                                {text: 'Setting up', link: '/setting-up'},
                                {text: 'Configuring reserved button', link: '/reserved-button'},
                                {text: 'Disabling filters', link: '/disabling'},
                            ]
                    }
                ]
            }
        },

        socialLinks: [
            {icon: 'github', link: 'https://github.com/ZiYueCommentary/Filters-API'},
            {icon: 'modrinth', link: 'https://modrinth.com/project/filters-api'},
            {icon: 'weblate', link: 'https://weblate.ziyuesinicization.site/engage/filters-api/'}
        ]
    }
})
