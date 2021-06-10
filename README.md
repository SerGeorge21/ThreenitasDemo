# ThreenitasDemo
My project for the interview at Threenitas

Networking:  
Τη λειτουργικότητα για τα 2 requests ( POST & GET ) την υλοποίησα με 2 λίγο διαφορετικούς τρόπους.  
--Το POST request γίνεται στην MainActivity μέσω ενός inner class που κάνει extend την AsyncTask. Το token που επιστρέφεται από το επιτυχές login περνιέται στο επόμενο activity (ListActivity) για να γίνε ιτο επόμενο request.  
--To GET request γίνεται στην ListActivity που κάνει implement την LoaderCallbacks<> και αρχικοποιεί έναν custom Loader. 
Ο BookLoader και η QueryUtils βρίσκονται σε ξεχωριστό package (com.example.threenitasdemo.networking). H QueryUtils είναι βοηθητική class που περιέχει τις μεθόδους από τις οποίες απαρτίζεται το GET request.\
Αντιλαμβάνομαι πλήρως πως το να χρησιμοποιήσω deprecated classes δεν είναι best practice. Θεώρησα όμως σημαντικό να δείξω πως κατανοώ ποια είναι η διαδικασία για να γίνουν post/get requests.\
Για αυτό προτίμησα να γράψω τα requests "στο χέρι" και δεν χρησιμοποίησα τεχνολογίες όπως πχ Retrofit που κάνουν τη διαδικασία περισσότερο black box.  

Login:  
Παρέχω βασικούς ελέγχους για τα username/password. Δυστυχώς δεν κατάλαβα πως να φτιάξω ένα δικό μου regular expression για να ελέγχω τις διάφορες απαιτήσεις που πρέπει να πληρούν τα πεδία.  

Layouts:  
activity_main => Βασικό Login Layout με 2 TextViews, 2 EditText, 1 (custom) Button.  

activity_list => GridView 2 στηλών.  

list_item => Layout που κάνει populate το κάθε item στo GridView.  

LoginActivity:  
Εδώ φαίνονται όλα τα βιβλία που "πήραμε" από το API.  
Πατώντας στο εικονίδιο με το κάτω βελάκι γίνεται download του βιβλίου χρησιμοποιώντας το pdf_url.  
Δυστυχώς το progress του downlaod δε φαινεται και δεν έρχεται μήνυμα ενημέρωσης μόλις τελειώνει, αλλά ανοίγωντας στο κινητό μου την εφαρμογή "Λήψεις",
όλα τα βιβλία που πάτησα κατεβαίνουν και φαίνονται εκεί κανονικά.  
Για να λύσω το παραπάνω θα χρησιμοποιούσα την κλάση BroadcastReceiver και κάποιες σχετικές μεθόδους της υπερκλάσης της LoginActivity (πχ registerReceiver).  
Η ταξινόμηση των books ανα ημερομηνία λείπει καθαρά γιατι δεν άφησα αρκετό χρόνο για να ασχοληθώ με αυτό. Θα έφτιαχνα ένα Date field variable στην Books που θα είχε στην ημερομηνία σε
κάποιο format. Με αυτό θα έκανα την ταξινόμηση των βιβλίων στην ArrayList<Book> είτε με κάποια έτοιμη μέθοδο, είτε φτιάχνοντας δική μου(πχ quicksort)
