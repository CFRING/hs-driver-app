package com.hongshi.wuliudidi.incomebook;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

//import javax.net.ssl.SSLSocketFactory;

/**
 * Created by van on 2016/5/3.
 */
public class HttpsUtilsForFinalHttp {
    public static SSLSocketFactory getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password)
    {
        try
        {
            KeyStore trustStore = prepareTrustStore(certificates);
            KeyStore keyStore = prepareKeyStore(bksFile, password);
            return new SSLSocketFactory(keyStore, password, trustStore);
        } catch (NoSuchAlgorithmException e)
        {
            throw new AssertionError(e);
        } catch (KeyManagementException e)
        {
            throw new AssertionError(e);
        } catch (KeyStoreException e)
        {
            throw new AssertionError(e);
        }
        catch(UnrecoverableKeyException e)
        {
            throw new AssertionError(e);
        }
    }

    private static KeyStore prepareTrustStore(InputStream...certificates){
        if (certificates == null || certificates.length <= 0) return null;
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)
                {
                }
            }
            return keyStore;
        }catch (CertificateException e){
            e.printStackTrace();
        }catch (KeyStoreException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static KeyStore prepareKeyStore(InputStream bksFile, String password){
        try
        {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            return clientKeyStore;

        } catch (KeyStoreException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(CertificateException e){
            e.printStackTrace();
        }
        return null;
    }
}
