import {useEffect, useState} from 'react';

import {hasExternalScanner} from '..';

export function useExternalScanner() {
  const [scannerConnected, setScannerConnected] = useState(false);

  useEffect(() => {
    async function existExternalScanner() {
      const response = await hasExternalScanner();

      setScannerConnected(response);
    }

    existExternalScanner();
  }, []);

  return {scannerConnected};
}
