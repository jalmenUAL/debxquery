(: Bug: $b/titles :)
<bib>
 {
  for $b in db:open('bstore1')/bib/book
  where $b/publisher = 'Addison-Wesley' and $b/@year > 1991
  return
    <book year='{ $b/@year }'>
     { $b/titles }
    </book>
 }
</bib> 