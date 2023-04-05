import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { TouchableHighlight } from 'react-native';
import { StyleSheet, Text, View } from 'react-native';
import TelrPayCheckout from './TelrCheckout';

export default function App() {
  return (
    <View style={styles.container}>

      <TouchableHighlight
        style={{ borderColor: 'gray', borderWidth: 0.8, borderRadius: 5, padding: 20 }}
        onPress={() => {

          var options = {
            merchantKey: "live-FP0RSX366",
            merchantSecret: "sec-FB0VLA3E8",
            customer_details: {
              name: "My Store",
              email: "golu.r@ulistechnology.com",
              mobile: "8909890986"
            },
            productDetails: {
              vendorName: "Zora Store",
              vendorMobile: "1122334455",
              productName: "T-Shirt",
              productPrice: 120.00,
              currency: "AED",
              image: "https://assetscdn1.paytm.com/images/catalog/product/A/AP/APPSCOTT-INTERNSWIT61083A52CCB4C/1562907534025_1..jpg?imwidth=320&impolicy=hq"
            },
            billing_details: {
              address_line1: "Wardhman nagar ,nagpur",
              address_line2: "",
              country: "India",
              city: "Nagpur",
              pin: "440001",
              province: "Maharastra"
            },
            shipping_details: {
              address_line1: "Wardhman nagar ,nagpur",
              address_line2: "",
              country: "India",
              city: "Nagpur",
              pin: "440001",
              province: "Maharastra"
            },
            order_details: {
              order_id: "ORD1680084434213",
              amount: "366.45",
              currency: "AED",
              description: "TShirt",
              return_url: "https://dev.tlr.fe.ulis.live/merchant/payment/status"
            },
            merchant_urls: {
              success: "https://ulis.live/status.php",
              cancel: "https://ulis.live/cancel.php",
              failure: "https://ulis.live/failed.php"
            },
          }

          TelrPayCheckout.open(options).then((data) => {
            // handle success
            console.log("Error: ", data)
            alert(`Success: ${data}`);
          }).catch((error) => {
            // handle failure
            console.log("Error: ", error)
            alert(`Error: ${error.description} `);
          });

        }}
      >
        <Text>PAY</Text>

      </TouchableHighlight>

      <StatusBar style="auto" />

    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
