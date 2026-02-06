declare module 'sockjs-client/dist/sockjs' {
  interface SockJSConstructor {
    new (url: string, protocols?: string | string[] | null, options?: Record<string, unknown>): WebSocket;
  }

  const SockJS: SockJSConstructor;
  export default SockJS;
}
