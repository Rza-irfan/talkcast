export {};

declare global {
  interface Window {
    google: typeof google;
  }

  const google: {
    accounts: {
      id: {
        initialize: (options: {
          client_id: string;
          callback: (response: CredentialResponse) => void;
          auto_select?: boolean;
        }) => void;
        renderButton: (
          container: HTMLElement,
          options: {
            theme: string;
            size: string;
            shape: string;
            text: string;
          }
        ) => void;
        prompt: () => void;
      };
    };
  };

  interface CredentialResponse {
    credential: string;
    clientId: string;
    select_by: string;
  }
}
