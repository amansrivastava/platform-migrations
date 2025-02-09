// vars/generateAccessToken.groovy

def call(String credentialsId, String tokenUrl) {

    // Use Jenkins credentials binding to access the stored credentials
    withCredentials([usernamePassword(credentialsId: credentialsId, 
                                      usernameVariable: 'CLIENT_ID', 
                                      passwordVariable: 'CLIENT_SECRET')]) {
        def connection = new URL(tokenUrl).openConnection()
        connection.setRequestMethod('POST')
        connection.setDoOutput(true)
        connection.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')

        def postData = "grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}"
        connection.outputStream.write(postData.getBytes("UTF-8"))
        def response = connection.inputStream.text
        def jsonResponse = new groovy.json.JsonSlurper().parseText(response)

        return jsonResponse.access_token
    }
}
